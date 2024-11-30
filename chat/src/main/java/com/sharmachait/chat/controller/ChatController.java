package com.sharmachait.chat.controller;

import com.sharmachait.chat.model.ChatMessage;
import com.sharmachait.chat.model.Notification;
import com.sharmachait.chat.model.User;
import com.sharmachait.chat.repository.UserRepository;
import com.sharmachait.chat.service.ChatMessageService;
import com.sharmachait.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable("senderId") Long senderId,
            @PathVariable("recipientId") Long recipientId
    ){
        try {
            List<ChatMessage> chatMessages = chatMessageService.getChatMessages(senderId,recipientId);
            return ResponseEntity.ok(chatMessages);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ){
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        Long userId = savedMessage.getRecipientId();
        User user = userRepository.findById(userId).orElse(null);
        Notification notification = Notification.builder()
                .id(savedMessage.getId())
                .recipientId(savedMessage.getRecipientId())
                .senderId(savedMessage.getSenderId())
                .message(savedMessage.getContent())
                .build();
        messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/messages",
                notification);
    }
}
