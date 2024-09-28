package com.example.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {

    @Id
    private String messageId;

    private String senderId;

    private String content;

    private LocalDateTime timestamp;

    public Message(String senderId, String content) {

        this.senderId = senderId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
