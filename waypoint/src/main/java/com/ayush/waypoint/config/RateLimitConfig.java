package com.ayush.waypoint.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ayush.waypoint.filter.RateLimitFilter;

@Configuration
public class RateLimitConfig {
    @Bean
    public RateLimitFilter rateLimitFilter() {
        return new RateLimitFilter();
    }
}