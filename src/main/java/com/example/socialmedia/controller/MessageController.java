package com.example.socialmedia.controller;

import com.example.socialmedia.dto.SendMessageRequest;
import com.example.socialmedia.dto.EditMessageRequest;
import com.example.socialmedia.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chats")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{username}/{chatId}/send")
    public ResponseEntity<?> sendMessage(@PathVariable String username, @PathVariable String chatId, @RequestBody SendMessageRequest sendMessageRequest) {

        // Validate message content
        isMessageValid(sendMessageRequest.message());

        // Send message
        messageService.sendMessage(chatId, username, sendMessageRequest.message());

        return ResponseEntity.ok("Message sent successfully.");
    }

    @PutMapping("/{username}/{chatId}/edit")
    public ResponseEntity<?> editMessage(@PathVariable String username, @PathVariable String chatId, @RequestBody EditMessageRequest editMessage) {

        isMessageValid(editMessage.message());

        messageService.editMessage(chatId, username, editMessage.messageId(), editMessage.message());

        return ResponseEntity.ok("Message edited successfully.");

    }

    @DeleteMapping("/{username}/{chatId}/delete")
    public ResponseEntity<?> deleteMessage(@PathVariable String username, @PathVariable String chatId, @RequestParam String messageId) {

        messageService.deleteMessage(username, chatId, messageId);

        return ResponseEntity.ok("Message deleted successfully.");
    }

    // Method to ensure message content isn't null or empty
    public void isMessageValid(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty.");
        }
    }
}
