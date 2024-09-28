package com.example.socialmedia.exception;

public class ChatNotFoundException extends RuntimeException{
    public ChatNotFoundException(String chatId) {
        super("Chat " + chatId + " not found.");
    }
}
