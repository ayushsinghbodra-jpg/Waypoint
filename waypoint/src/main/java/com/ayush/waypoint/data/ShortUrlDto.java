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
        private final Long count;

        public Res(Long count, String encode) {
            this.url = encode;
            this.count = count;
        }

        public String getUrl() {
            return url;
        }

        public Long getCount() {
            return count;
        }
    }
}