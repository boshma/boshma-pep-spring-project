package com.example.service;

import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        // MessageText must not be blank and must be less than 255 characters
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
                message.getMessageText().length() > 255) {
            throw new BadRequestException("Invalid message text");
        }

        // Check if the account exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new BadRequestException("User does not exist");
        }

        // Save the new message
        return messageRepository.save(message);
    }

    // Method to retrieve all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Method to retrieve a message by ID
    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);  // Return null if not found
    }

    // Method to retrieve all messages posted by a specific user
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    // Delete a message by its ID
    public boolean deleteMessage(Integer messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            messageRepository.deleteById(messageId);
            return true; // Message deleted successfully
        } else {
            return false; // Message not found, but we return true to indicate idempotency
        }
    }

    // Update a message by its ID
    public Message updateMessage(Integer messageId, String newText) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            if (newText == null || newText.isBlank() || newText.length() > 255) {
                throw new BadRequestException("Invalid message text");
            }
            message.setMessageText(newText);
            return messageRepository.save(message);
        } else {
            throw new BadRequestException("Message not found");
        }
    }
}