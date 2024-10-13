package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // POST /register - Register a new user
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registeredAccount = accountService.register(account);
        return ResponseEntity.ok(registeredAccount);
    }

    // POST /login - Log in a user
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
        return ResponseEntity.ok(loggedInAccount);
    }

    // POST /messages - Create a new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message newMessage = messageService.createMessage(message);
        return ResponseEntity.ok(newMessage);
    }

    // GET /messages - Get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // GET /messages/{messageId} - Get a message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().body(null);  // Return 200 OK with an empty body if not found
        } else {
            return ResponseEntity.ok(message);  // Return the message with 200 OK if found
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        boolean isDeleted = messageService.deleteMessage(messageId);
        if (isDeleted) {
            return ResponseEntity.ok(1);  // Return 1 if the message was deleted
        } else {
            return ResponseEntity.ok().build();  // Return 200 OK with no body if the message did not exist
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message updatedMessage) {
        Message updated = messageService.updateMessage(messageId, updatedMessage.getMessageText());
        if (updated != null) {
            return ResponseEntity.ok(1); // Return 1 row modified
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if the message is not found
        }
    }

    // GET /accounts/{accountId}/messages - Get all messages by a specific user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }
}