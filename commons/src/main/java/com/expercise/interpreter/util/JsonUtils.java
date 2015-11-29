package com.expercise.interpreter.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;

public final class JsonUtils {

    private JsonUtils() {
    }

    public static String toJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception ignored) {
        }

        return object instanceof Collection ? "[]" : "{}";
    }

    public static <T> T fromJsonString(String jsonString, Class<T> targetClass) {
        try {
            return new ObjectMapper().readValue(jsonString, targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
