package com.ayush.waypoint.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@EntityListeners(value = AuditingEntityListener.class)
@SequenceGenerator(name = "seq", initialValue = 100000)
public class ShortUrl {
     
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created;

    @Column(length = 1000, unique = true, nullable = false)
    private String url;

    @Column(nullable = false)
    private Long count = 0L;

    protected ShortUrl() {
    }

    public ShortUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getUrl() {
        return url;
    }

    public Long getCount() {
        return count;
    }

    public void increaseCount() {
        this.count++;
    }
}