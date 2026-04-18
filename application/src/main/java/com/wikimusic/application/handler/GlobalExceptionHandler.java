package com.wikimusic.application.handler;

import com.wikimusic.domain.exception.ArtistNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GlobalExceptionHandler — Translates domain exceptions into HTTP responses.
 *
 * This keeps error handling at the application boundary, ensuring
 * the domain layer remains clean of HTTP concerns.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ArtistNotFoundException} and returns HTTP 404.
     *
     * @param ex the thrown exception
     * @return a 404 response with error details
     */
    @ExceptionHandler(ArtistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleArtistNotFound(ArtistNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        LocalDateTime.now()));
    }

    /**
     * Handles bean validation errors and returns HTTP 400.
     *
     * @param ex the thrown exception
     * @return a 400 response with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        String.join("; ", errors),
                        LocalDateTime.now()));
    }

    /**
     * Handles illegal argument errors and returns HTTP 400.
     *
     * @param ex the thrown exception
     * @return a 400 response with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        LocalDateTime.now()));
    }

    /**
     * Catches all unhandled exceptions and returns HTTP 500.
     *
     * @param ex the thrown exception
     * @return a 500 response with a generic message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unexpected error occurred",
                        LocalDateTime.now()));
    }

    /**
     * Standard error response body.
     */
    public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}
