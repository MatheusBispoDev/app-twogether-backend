package com.app.us_twogether.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<String> handleDataAlreadyExistsException(DataAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public  ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // 409 Conflict
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(DataNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 Not Found
                .body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleDataNotFoundException(HttpMessageNotReadableException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                .body(ex.getMessage());
    }

    @ExceptionHandler(TokenBlacklistedException.class)
    public ResponseEntity<String> handleTokenBlacklistedException(TokenBlacklistedException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 409 Conflict
                .body(ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<String> handleTokenInvalidException(TokenInvalidException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 409 Conflict
                .body(ex.getMessage());
    }
}