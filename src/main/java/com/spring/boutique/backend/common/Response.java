package com.spring.boutique.backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Standard wrapper for every API response.
 * Frontend can always expect: { success, message, data, timestamp }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private boolean success;
    private String message;
    private T data;
    private Instant timestamp;

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(true, message, data, Instant.now());
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, "OK", data, Instant.now());
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(false, message, null, Instant.now());
    }
}
