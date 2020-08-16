package com.seat.code.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJsonUtils {

    private TestJsonUtils() {
        // this class is not meant to be instantiated as it's a util class
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
