package com.example.socialmedia.repository;

import com.example.socialmedia.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message, String> {
    Message findByMessageId(String messageId);
}
