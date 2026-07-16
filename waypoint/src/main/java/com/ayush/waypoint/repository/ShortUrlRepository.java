package com.ayush.waypoint.repository;

import com.ayush.waypoint.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

pupblic interface ShortUrlRpository extends JpaRepository<ShortUrl , Long> {
    Optional<ShortUrl> findByUrl(String url);
}
