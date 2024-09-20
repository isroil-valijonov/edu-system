package com.example.edusystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> phoneNumberNotFoundException(UsernameNotFoundException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", ex.getMessage());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd - HH.mm.ss")));

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> notFoundException(CustomNotFoundException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", ex.getMessage());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd - HH.mm.ss")));

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", e.getMessage());
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd - HH.mm.ss")));

        return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
    }

}
