package com.example.socialmedia.repository;

import com.example.socialmedia.model.Chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String> {
    Chat findByChatId(String chatId);
}
