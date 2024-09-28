package com.example.socialmedia.service;

import com.example.socialmedia.model.Chat;
import com.example.socialmedia.model.Message;

import java.util.ArrayList;
import java.util.List;

public interface ChatService {
    void createChat(String sender, ArrayList<String> receiver);

    void addParticipant(String username, String chatId, String participant);

    void removeParticipant(String username, String chatId, String participant);

    List<Message> getChatMessages(String username, String chatId);

    List<Message> getChatMessagesForUser(String username, String chatId, String participant);

    Chat findChatById(String chatId);
}
