package ru.clevertec.sessionservice.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        String message = String.format("%s %s", LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
