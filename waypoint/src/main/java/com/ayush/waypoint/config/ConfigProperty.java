package com.ayush.waypoint.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@COnfiguration
@COnfigurationProperties(prefix = "short-server")
public class COnfigProperty {
    private String baseUrl;

    public Stirng getBaseUrl(){
        return baseUrl;
    }

    public void setbaseUrl(string baseUrl) {
        this.baseUrl = baseUrl;
    }
}