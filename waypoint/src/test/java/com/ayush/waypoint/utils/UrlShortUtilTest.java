package com.ayush.waypoint.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortUtilTest {

    private final UrlShortUtil urlShortUtil = new UrlShortUtil("test-salt-value");

    @Test
    void roundTripEncodeDecodeForBoundaryValues() {
        long[] ids = {100000L, 999999999L};

        for (long id : ids) {
            String encoded = urlShortUtil.encode(id);
            long decoded = urlShortUtil.decode(encoded);
            assertEquals(id, decoded, "decode(encode(id)) should return the original id for id=" + id);
        }
    }

    @Test
    void decodeWithInvalidCharactersThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> urlShortUtil.decode("A1B2"),
                "Decoding an invalid string should throw IllegalArgumentException");
    }

    @Test
    void encodeRangeProducesUniqueValues() {
        Set<String> encodedSet = new HashSet<>();
        long start = 100000L;
        long end = 100999L;

        for (long id = start; id <= end; id++) {
            String encoded = urlShortUtil.encode(id);
            assertTrue(encodedSet.add(encoded), "Collision detected for id: " + id + ", encoded value: " + encoded);
        }

        assertEquals((int) (end - start + 1), encodedSet.size(), "All encoded values in the range should be unique");
    }
}
