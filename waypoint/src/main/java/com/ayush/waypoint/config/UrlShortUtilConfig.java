package com.ayush.waypoint.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ayush.waypoint.utils.UrlShortUtil;

@Configuration
public class UrlShortUtilConfig {
    @Bean
    public UrlShortUtil urlShortUtil(ConfigProperty configProperty) {
        return new UrlShortUtil(configProperty.getHashIdSalt());
    }    
}