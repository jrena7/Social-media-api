package com.example.socialmedia.repository;

import com.example.socialmedia.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    Chat findByChatId(String chatId);
}
