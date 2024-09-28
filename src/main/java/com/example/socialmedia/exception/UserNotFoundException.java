package com.example.socialmedia.exception;

// Exception for when a user cannot be found
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super("User " + username + " not found.");
    }
}
