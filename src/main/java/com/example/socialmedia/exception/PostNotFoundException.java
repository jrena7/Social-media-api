package com.example.socialmedia.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String postId) {
        super("Post " + postId + " not found.");
    }
}
