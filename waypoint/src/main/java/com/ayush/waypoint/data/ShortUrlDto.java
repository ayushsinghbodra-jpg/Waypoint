package com.ayush.waypoint.data;

import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ShortUrlDto {
    public static class Req {
        @NotEmpty(message = "Url must be not empty")
        @Size(max = 1000, message = "Url length must be under 1000")
        @URL(message = "Url is invalid")
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Res {
        private final String url;

        public Res( String encode) {
            this.url = encode;   
        }

        public String getUrl() {
            return url;
        }
    }

    public static class StatsRes {
        private final Long count;

        public StatsRes(Long count){
            this.count = count;
        }

        public Long getCount() {
            return count;
        }
    }
}