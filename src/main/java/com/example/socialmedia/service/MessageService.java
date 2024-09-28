package com.example.socialmedia.service;

public interface MessageService {
    void sendMessage(String chatId, String sender, String content);

    void editMessage(String chatId, String username, String messageId, String content);

    void deleteMessage(String username, String chatId ,String messageId);
}
