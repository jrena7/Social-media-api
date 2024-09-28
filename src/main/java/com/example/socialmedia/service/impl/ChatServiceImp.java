package com.example.socialmedia.service.impl;

import com.example.socialmedia.exception.ChatNotFoundException;
import com.example.socialmedia.model.Chat;
import com.example.socialmedia.model.Message;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.ChatRepository;
import com.example.socialmedia.repository.MessagesRepository;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.service.ChatService;
import com.example.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImp implements ChatService {

    @Autowired
    private UserRepository userRepository;;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public void createChat(String sender, ArrayList<String> receivers) {
        // Create chat and save
        Chat chat = new Chat();
        chatRepository.save(chat);

        receivers.add(sender); // Add sender to list of receivers/ participants
        ArrayList<User> savedUsers = new ArrayList<>(); // List of users for save all

        // Validate all users, add them as participant and add chat to their list of chats
        for (String receiver : receivers) {
            User user = userService.findUserByName(receiver);
            chat.addParticipant(user.getId());
            user.addChat(chat.getChatId());
            savedUsers.add(user);
        }

        // Save if all is valid
        userRepository.saveAll(savedUsers);
        chatRepository.save(chat);
    }

    @Override
    public void addParticipant(String username, String chatId, String participant) {
        // Find user by username
        User user = userService.findUserByName(username);
        User part = userService.findUserByName(participant);

        // Find chat by Id
        Chat chat = findChatById(chatId);

        // Check if user is a participant in the chat
        if (!chat.isParticipant(user.getId())) {
            throw new IllegalArgumentException(username + " is not a participant in the chat.");
        }

        // Check if participant is already a participant in the chat
        if (chat.isParticipant(part.getId())) {
            throw new IllegalArgumentException(participant + " is already a participant in the chat.");
        }

        // Add user to chat
        chat.addParticipant(part.getId());
        part.addChat(chat.getChatId());

        userRepository.save(part);
        chatRepository.save(chat);
    }

    @Override
    public void removeParticipant(String username, String chatId, String participant) {
        User user = userService.findUserByName(participant);
        User part = userService.findUserByName(participant);

        Chat chat = findChatById(chatId);

        if (!chat.isParticipant(user.getId())) {
            throw new IllegalArgumentException(username + " is not a participant in the chat.");
        }

        // Participant is already not a participant
        if (!chat.isParticipant(part.getId())) {
            throw new IllegalArgumentException(participant + " is already not a participant in the chat.");
        }

        // Remove participant from chat
        part.removeChat(chat.getChatId());
        chat.removeParticipant(part.getId());

        userRepository.save(part);

        // Delete chat if no participants / user was the last participant
        if (chat.getParticipantIds().isEmpty()) {
            chatRepository.delete(chat);
        } else {
            chatRepository.save(chat);
        }
    }

    // Get all messages in a chat
    @Override
    public List<Message> getChatMessages(String username, String chatId) {
        User user = userService.findUserByName(username);

        Chat chat = findChatById(chatId);

        if (!chat.isParticipant(user.getId())) {
            throw new IllegalArgumentException(username + " is not a participant in the chat.");
        }

        // Get all messages in chat
        return messagesRepository.findAllById(chat.getMessageIds());
    }

    // Get all messages in a chat of a specific user
    @Override
    public List<Message> getChatMessagesForUser(String username, String chatId, String participant) {

        User user = userService.findUserByName(username);
        User part = userService.findUserByName(participant);

        Chat chat = findChatById(chatId);

        if (!chat.isParticipant(user.getId())) {
            throw new IllegalArgumentException(participant + " is not a participant in the chat.");
        }

        // Get all messages in chat
        List <Message> chatMessages = getChatMessages(username, chatId);
        List <Message> partMessages = new ArrayList<>();

        // Filter messages for specific user
        for (Message message : chatMessages) {
            if (message.getSenderId().equals(part.getId())) {
                partMessages.add(message);
            }
        }

        // Return messages of that user
        return partMessages;
    }

    @Override
    public Chat findChatById(String chatId) {
        Chat chat = chatRepository.findByChatId(chatId);

        if (chat == null) {
            throw new ChatNotFoundException(chatId);
        }

        return chat;
    }
}
