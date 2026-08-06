package com.ayush.waypoint.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "short-server")
public class ConfigProperty {
    private String baseUrl;
    private String hashIdSalt;
    
    public String getHashIdSalt() {
        return hashIdSalt;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setHashIdSalt(String hashIdSalt) {
        this.hashIdSalt = hashIdSalt;
    }
}