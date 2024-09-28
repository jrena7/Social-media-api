package com.example.socialmedia.dto;

public record EditMessageRequest(
        String messageId,

        String message
) {
}
