package com.ayush.waypoint.service;

import com.ayush.waypoint.domain.ShortUrl;
import com.ayush.waypoint.exception.NotFoundException;
import com.ayush.waypoint.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

@Service
public class ShortUrlService {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlService.class);
    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository) {
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

    @Cacheable(value = "urls", key = "#id")
    @Transactional(readOnly = true)
    public String findById(Long id) {
        log.info("Cache MISS — hitting DB for id: {}", id);
        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invalid short URL id: " + id));
        return shortUrl.getUrl();
    }

    @CacheEvict(value = "urls", key = "#id")
    @Transactional
    public void deleteById(Long id) {
        if (!shortUrlRepository.existsById(id)) {
            throw new NotFoundException("Short URL not found with id: " + id);
        }
        shortUrlRepository.deleteById(id);
    }

    @Transactional
    public void increaseCount(Long id) {
        shortUrlRepository.incrementCount(id);
    }


    @Transactional(readOnly = true)
    public ShortUrl findShortUrlById(Long id) {
        return shortUrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invalid short URL id: " + id));
    }
}