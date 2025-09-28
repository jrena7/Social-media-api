package com.example.socialmedia.model;

import lombok.Data;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    private String chatId;

    @ElementCollection
    private List<String> participantIds;

    @ElementCollection
    private List<String> messageIds;

    public Chat() {
        this.participantIds = new ArrayList<>();
        this.messageIds = new ArrayList<>();
    }

    public void addParticipant(String participantId) {
        this.participantIds.add(participantId);
    }

    public void removeParticipant(String participantId) {
        this.participantIds.remove(participantId);
    }

    public boolean isParticipant(String participantId) {
        return this.participantIds.contains(participantId);
    }

    public void addMessage(String messageId) {
        this.messageIds.add(messageId);
    }

    public void removeMessage(String messageId) {
        this.messageIds.remove(messageId);
    }

    public boolean hasMessage(String messageId) {
        return this.messageIds.contains(messageId);
    }

}
