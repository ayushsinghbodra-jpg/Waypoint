package com.ayush.waypoint.repository;

import com.ayush.waypoint.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByUrl(String url);

    @Modifying 
    @Query("UPDATE ShortUrl s SET s.count = s.count + 1 WHERE s.id = :id")
    void incrementCount(@Param("id") Long id);
}

