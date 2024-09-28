package com.example.socialmedia.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String messageId) {
        super("Message " + messageId + " not found.");
    }
}
