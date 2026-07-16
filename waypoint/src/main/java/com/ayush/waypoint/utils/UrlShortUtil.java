package com.ayush.waypoint.utils;


public final class UrlShortUtil {
    public static final String ALPHABET = "bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
    public static final int BASE = ALPHABET.length();

    public static String encode(Long num) {
        StringBuilder sb = new StringBuilder();
        while(num>0){
            sb.append(ALPHABET.charAt((int)(num%BASE)));
            num=num/BASE;
        }
        return sb.toString();
    }

    public static Long decode(String str) {
        Long num = 0L;
        int power = 1;

        for(int i=0 i<str.length(); i++) {
            num = num + ((Long)ALPHABET.indexof(str.charAt(i)))*power;
            power= power*BASE;
        }

        return num;
    }
}