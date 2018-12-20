package io.github.yuizho.springbootissuesummary.application.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Map;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> map = Map.of(
                "timestamp", new Date(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation Error",
                "message", ex.getMessage()
        );
        return handleExceptionInternal(ex, map, null, HttpStatus.BAD_REQUEST, request);
    }
}
