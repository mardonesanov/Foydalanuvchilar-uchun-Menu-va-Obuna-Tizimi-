package com.example.demo.service;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found!"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found!"));

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        messageRepository.save(message);
    }

    public List<Map<String, Object>> getChatList(Long userId) {
        List<Object[]> rawChats = messageRepository.findChatList(userId);
        List<Map<String, Object>> chats = new ArrayList<>();
        for (Object[] rawChat : rawChats) {
            Map<String, Object> chat = new HashMap<>();
            chat.put("chatPartnerId", rawChat[0]);
            chat.put("chatPartnerName", rawChat[1]);
            chat.put("lastMessage", rawChat[2]);
            chat.put("lastMessageTime", rawChat[3]);
            chat.put("unreadCount", rawChat[4]);
            chats.add(chat);
        }
        return chats;
    }

    public List<Message> getMessages(Long senderId, Long receiverId) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }

    public void markMessagesAsRead(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findUnreadMessages(senderId, receiverId);
        for (Message message : messages) {
            message.setRead(true);
        }
        messageRepository.saveAll(messages);
    }

public void deleteChat(Long userId, Long chatPartnerId) {
        messageRepository.deleteMessagesBetweenUsers(userId, chatPartnerId);
    }
}
