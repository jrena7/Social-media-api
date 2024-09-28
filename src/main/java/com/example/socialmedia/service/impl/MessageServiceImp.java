package com.example.socialmedia.service.impl;

import com.example.socialmedia.exception.MessageNotFoundException;
import com.example.socialmedia.model.Chat;
import com.example.socialmedia.model.Message;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.ChatRepository;
import com.example.socialmedia.repository.MessagesRepository;
import com.example.socialmedia.service.ChatService;
import com.example.socialmedia.service.MessageService;
import com.example.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImp implements MessageService {

    private final MessagesRepository messagesRepository;

    private final ChatRepository chatRepository;

    private final UserService userService;

    private final ChatService chatService;

    @Autowired
    public MessageServiceImp(MessagesRepository messagesRepository, ChatRepository chatRepository, UserService userService, ChatService chatService) {
        this.messagesRepository = messagesRepository;
        this.chatRepository = chatRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Transactional
    @Override
    public void sendMessage(String chatId, String sender, String content) {
        // Get the sender user + the chat they want to send the message to
        User user = userService.findUserByName(sender);
        Chat chat = chatService.findChatById(chatId);

        if (!chat.isParticipant(user.getId())) {
            throw new IllegalArgumentException(sender + " is not a participant in the chat.");
        }

        // Create the message and save
        Message message = new Message(user.getId(), content);
        messagesRepository.save(message);

        // Add the message to the chat and save
        chat.addMessage(message.getMessageId());
        chatRepository.save(chat);
    }

    @Override
    public void editMessage(String chatId, String username, String messageId, String content) {
        // Check if the message is in the chat
        messageInChat(chatId, messageId);

        // Get the message and check if it belongs to the user
        Message message = findMessageById(messageId);
        messageBelongsToUser(username, message);

        // Update the message content and save
        message.setContent(content);
        messagesRepository.save(message);
    }

    @Override
    public void deleteMessage(String username, String chatId, String messageId) {
        messageInChat(chatId, messageId);

        Message message = findMessageById(messageId);
        messageBelongsToUser(username, message);

        // Remove the message from the chat and save
        Chat chat = chatService.findChatById(chatId);
        chat.removeMessage(messageId);
        chatRepository.save(chat);

        // Delete the message from repository
        messagesRepository.delete(message);
    }

    // Helper methods
    public Message findMessageById(String messageId) {
        Message message = messagesRepository.findByMessageId(messageId);

        if (message == null) {
            throw new MessageNotFoundException(messageId);
        }

        return message;
    }

    // Check if the message is in the chat
    private void messageInChat(String chatId, String messageId) {
        Chat chat = chatService.findChatById(chatId);

        if (!chat.hasMessage(messageId)) {
            throw new IllegalArgumentException("Message not in chat. Ensure the message is in the chat.");
        }
    }

    // Check if the message belongs to the user
    private void messageBelongsToUser(String username, Message message) {
        User user = userService.findUserByName(username);

        if (!message.getSenderId().equals(user.getId())) {
            throw new IllegalArgumentException("Cannot change a message that is not yours.");
        }
    }
}
