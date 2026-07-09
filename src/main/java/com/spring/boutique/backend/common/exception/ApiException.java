package com.spring.boutique.backend.common.exception;

import org.springframework.http.HttpStatus;

 * Generic exception for business-rule violations.
 * Throw this anywhere in a service layer with a status + message,
 * and GlobalExceptionHandler will turn it into a clean Response<T>.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
