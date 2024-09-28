package com.example.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "chats")
public class Chat {

    @Id
    private String chatId;

    private List<String> participantIds;

    private List<String> messageIds;

    public Chat (){
        this.participantIds = new ArrayList<>();
        this.messageIds = new ArrayList<>();
    }

    public void addParticipant(String participantId){
        this.participantIds.add(participantId);
    }

    public void removeParticipant(String participantId){
        this.participantIds.remove(participantId);
    }

    public boolean isParticipant(String participantId){
        return this.participantIds.contains(participantId);
    }

    public void addMessage(String messageId){
        this.messageIds.add(messageId);
    }

    public void removeMessage(String messageId){
        this.messageIds.remove(messageId);
    }

    public boolean hasMessage(String messageId){
        return this.messageIds.contains(messageId);
    }

}
