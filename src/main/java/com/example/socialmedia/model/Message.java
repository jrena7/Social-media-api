package com.example.socialmedia.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
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

    public boolean isSender(String senderId) {
        return this.senderId.equals(senderId);
    }
}
