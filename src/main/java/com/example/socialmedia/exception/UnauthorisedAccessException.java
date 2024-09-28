package com.example.socialmedia.exception;

public class UnauthorisedAccessException extends RuntimeException {
    public UnauthorisedAccessException(String username) {
        super(username + " does not have permission to access this resource.");
    }
}
