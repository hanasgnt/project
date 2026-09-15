package com.bootcamp.project.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.bootcamp.project.dto.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {

        private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
                String message = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getDefaultMessage())
                                .findFirst()
                                .orElse("Validation error");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.<String>builder()
                                                .success(false)
                                                .message(message)
                                                .data(null)
                                                .build());
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
                String message = ex.getConstraintViolations().stream()
                                .map(violation -> violation.getMessage())
                                .findFirst()
                                .orElse("Validation error");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.<String>builder()
                                                .success(false)
                                                .message(message)
                                                .data(null)
                                                .build());
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiResponse<String>> handleApiException(ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode())
                                .body(ApiResponse.<String>builder()
                                                .success(false)
                                                .message(ex.getReason())
                                                .data(null)
                                                .build());
        }

        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<ApiResponse<String>> handleGenericException(Exception
        // ex) {

        // logger.error("Unhandled exception occurred", ex);

        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body(ApiResponse.<String>builder()
        // .success(false)
        // .message("Terjadi kesalahan pada server. Silakan coba lagi nanti.")
        // .data(null)
        // .build());
        // }
}