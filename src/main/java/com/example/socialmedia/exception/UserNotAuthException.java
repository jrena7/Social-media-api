package com.example.socialmedia.exception;

public class UserNotAuthException extends RuntimeException {
    public UserNotAuthException(String username) {
        super("Could not authenticated User: " + username + ", please check your credentials.");
    }
}
