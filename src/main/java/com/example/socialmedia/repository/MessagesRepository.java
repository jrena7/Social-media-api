package com.example.socialmedia.repository;

import com.example.socialmedia.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessagesRepository extends MongoRepository<Message, String> {
    Message findByMessageId(String messageId);
}
