package com.ayush.waypoint.utils;

public final class UrlShortUtil {
    public static final String ALPHABET = "bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
    public static final int BASE = ALPHABET.length();

    public static String encode(Long num) {
        if (num == null || num <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        long value = num;
        while (value > 0) {
            int index = (int) (value % BASE);
            sb.append(ALPHABET.charAt(index));
            value = value / BASE;
        }
        return sb.reverse().toString();
    }

    public static Long decode(String str) {
        if (str == null || str.isEmpty()) {
            return 0L;
        }

        long num = 0L;
        for (int i = 0; i < str.length(); i++) {
            int index = ALPHABET.indexOf(str.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("Invalid short URL value: " + str);
            }
            num = num * BASE + index;
        }

        return num;
    }
}