package com.ayush.waypoint.utils;
import org.hashids.Hashids;
public final class UrlShortUtil {
    private final Hashids hashids;
    public UrlShortUtil(String salt) {
        this.hashids = new Hashids(salt, 10);
    }
    public String encode(Long id){
        return hashids.encode(id);
    }
    public Long decode(String shortUrl){
        long[] decoded = hashids.decode(shortUrl);
        if(decoded.length == 0){
            throw new IllegalArgumentException("Invalid short url");
        }
        return decoded[0];
    }
}