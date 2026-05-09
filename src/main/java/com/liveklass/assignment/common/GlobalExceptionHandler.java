package com.liveklass.assignment.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {

        ErrorResponse response = new ErrorResponse("BAD_REQUEST", e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}