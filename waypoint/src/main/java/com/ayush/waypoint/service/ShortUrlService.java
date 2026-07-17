package com.ayush.waypoint.service;

import com.ayush.waypoint.config.ConfigProperty;
import com.ayush.waypoint.domain.ShortUrl;
import com.ayush.waypoint.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ShortUrlService {

    private final ConfigProperty configProperty;
    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ConfigProperty configProperty, ShortUrlRepository shortUrlRepository) {
        this.configProperty = configProperty;
        this.shortUrlRepository = shortUrlRepository;
    }

    @Transactional
    public ShortUrl findByOriginalUrl(String url) {
        Optional<ShortUrl> existingShortUrl = shortUrlRepository.findByUrl(url);
        if (existingShortUrl.isPresent()) {
            return existingShortUrl.get();
        }

        ShortUrl shortUrl = new ShortUrl(url);
        return shortUrlRepository.save(shortUrl);
    }

    @Transactional(readOnly = true)
    public String findById(Long id) {
        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid short URL id: " + id));
        return shortUrl.getUrl();
    }
}