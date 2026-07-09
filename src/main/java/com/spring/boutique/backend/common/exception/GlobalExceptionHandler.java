package com.spring.boutique.backend.common.exception;

import com.spring.boutique.backend.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Catches every exception thrown anywhere in the app and converts it
 * into a clean Response<T> — the client never sees a raw stack trace.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Custom business exceptions (ApiException + subclasses like ResourceNotFoundException)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Response<Object>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(Response.error(ex.getMessage()));
    }

    // @Valid / @NotBlank etc. validation failures -> field-level error map
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        Response<Map<String, String>> response =
                new Response<>(false, "Validation failed", errors, java.time.Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Response.error("Invalid email or password"));
    }

    // Catch-all: anything unexpected -> generic 500, no stack trace leaked
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error("Something went wrong. Please try again later."));
    }
}
