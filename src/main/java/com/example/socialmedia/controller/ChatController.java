package com.example.socialmedia.controller;

import com.example.socialmedia.dto.ChatRequest;
import com.example.socialmedia.model.Message;
import com.example.socialmedia.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{username}/create")
    public ResponseEntity<?> createChat(@PathVariable String username, @RequestBody ChatRequest chatRequest) {

        // Cannot create an empty chat
        if (chatRequest.receivers() == null || chatRequest.receivers().isEmpty()) {
            throw new IllegalArgumentException("Cannot create chat without participants.");
        }

        // Create chat
        chatService.createChat(username, chatRequest.receivers());

        // Return success message
        return ResponseEntity.ok("Chat created successfully.");
    }

    @PostMapping("/{username}/{chatId}/add")
    public ResponseEntity<?> addParticipant(@PathVariable String username, @PathVariable String chatId, @RequestParam String participant) {

        // Add participant
        chatService.addParticipant(username, chatId, participant);

        // Return success message
        return ResponseEntity.ok("Participant added successfully.");
    }

    @PostMapping("/{username}/{chatId}/remove")
    public ResponseEntity<?> removeParticipant(@PathVariable String username, @PathVariable String chatId, @RequestParam String participant) {

        // Remove participant
        chatService.removeParticipant(username, chatId, participant);

        // Return success message
        return ResponseEntity.ok("Participant removed successfully.");
    }

    @GetMapping("/{username}/{chatId}")
    public ResponseEntity<?> getChatMessages(@PathVariable String username, @PathVariable String chatId) {

        // Get chat messages
        List<Message> messages = chatService.getChatMessages(username, chatId);

        if (messages.isEmpty()) {
            // Empty chat
            return ResponseEntity.ok("No messages found.");
        } else {
            return ResponseEntity.ok(messages);
        }
    }

    @GetMapping("/{username}/{chatId}/participant")
    public ResponseEntity<?> getChatMessagesForUser(@PathVariable String username, @PathVariable String chatId, @RequestParam String participant) {

        // Get chat messages for user
        List<Message> messages = chatService.getChatMessagesForUser(username, chatId, participant);

        return ResponseEntity.ok(messages);
    }
}
