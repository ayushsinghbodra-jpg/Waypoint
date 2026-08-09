package com.ayush.waypoint.filter;

import com.ayush.waypoint.utils.ApiUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class RateLimitFilter extends HttpFilter {

    private static final int CAPACITY = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);
    private static final Duration IDLE_EVICTION_THRESHOLD = Duration.ofMinutes(10);
    private static final Duration SWEEP_INTERVAL = Duration.ofMinutes(5);

    // Map holds our own wrapper (BucketEntry), NOT a raw Bucket — BucketEntry
    // does not implement the Bucket interface, it just carries one plus a
    // last-access timestamp for idle eviction.
    private final ConcurrentHashMap<String, BucketEntry> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong lastSweepTime = new AtomicLong(System.currentTimeMillis());

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        maybeSweepStaleBuckets();

        String clientKey = getClientKey(request);
        BucketEntry entry = buckets.computeIfAbsent(clientKey, k -> new BucketEntry(newBucket()));
        entry.touch();

        if (entry.bucket.tryConsume(1)) {
            chain.doFilter(request, response);
            return;
        }

        writeRateLimitResponse(response);
    }

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(CAPACITY, Refill.greedy(CAPACITY, WINDOW));
        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void writeRateLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiUtils.ApiResult<Void> body = ApiUtils.error(
                "Rate limit exceeded. Please try again later.",
                HttpStatus.TOO_MANY_REQUESTS
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.getWriter().flush();
    }

    /**
     * Evicts buckets untouched for longer than IDLE_EVICTION_THRESHOLD, at
     * most once per SWEEP_INTERVAL. This runs inline on request threads
     * rather than a background scheduler, but only actually does work every
     * few minutes, not on every request.
     */
    private void maybeSweepStaleBuckets() {
        long now = System.currentTimeMillis();
        long lastSweep = lastSweepTime.get();

        // NOTE: this must be "<", not ">" — we want to skip the sweep when
        // NOT enough time has passed, and actually run it once it has.
        if (now - lastSweep < SWEEP_INTERVAL.toMillis()) {
            return;
        }
        if (!lastSweepTime.compareAndSet(lastSweep, now)) {
            return; // another thread already claimed this sweep
        }

        Instant cutoff = Instant.now().minus(IDLE_EVICTION_THRESHOLD);
        buckets.entrySet().removeIf(e -> e.getValue().lastAccess.get().isBefore(cutoff));
    }

    /** Wraps a Bucket with a last-access timestamp for idle eviction. */
    private static final class BucketEntry {
        final Bucket bucket;
        final AtomicReference<Instant> lastAccess = new AtomicReference<>(Instant.now());

        BucketEntry(Bucket bucket) {
            this.bucket = bucket;
        }

        void touch() {
            lastAccess.set(Instant.now());
        }
    }
}