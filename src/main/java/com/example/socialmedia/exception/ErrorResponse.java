package com.example.socialmedia.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;
    private String error;
    private HttpStatus status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, String error, HttpStatus status) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
