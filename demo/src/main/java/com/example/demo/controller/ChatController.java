package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;
    @GetMapping("/list/{userId}")
    public String showChatList(@PathVariable Long userId, Model model) {
        List<Map<String, Object>> chats = messageService.getChatList(userId).stream().map(chat -> {
            Map<String, Object> chatMap = new HashMap<>();
            chatMap.put("chatPartnerId", chat.get("chatPartnerId"));
            chatMap.put("chatPartnerName", chat.get("chatPartnerName"));
            chatMap.put("lastMessage", chat.get("lastMessage"));
            chatMap.put("lastMessageTime", chat.get("lastMessageTime"));
            chatMap.put("unreadCount", chat.get("unreadCount"));
            chatMap.put("isExistingChat", true);
            return chatMap;
        }).collect(Collectors.toList());
        model.addAttribute("chats", chats);
        model.addAttribute("userId", userId);
        model.addAttribute("isSearchResult", false);
        return "chat_list";
    }
    @GetMapping("/messages/{senderId}/{receiverId}")
    public String showMessages(@PathVariable Long senderId,
                               @PathVariable Long receiverId,
                               @RequestParam(required = false) String receiverName,
                               Model model) {
        messageService.markMessagesAsRead(senderId, receiverId);

        if (receiverName == null || receiverName.isEmpty()) {
            User receiver = userService.findById(receiverId);
            if (receiver != null) {
                receiverName = receiver.getUsername(); // Receiver nomini oling
            } else {
                receiverName = "Noma'lum Foydalanuvchi";
            }
        }
        User receiver = userService.findById(receiverId);
        model.addAttribute("messages", messageService.getMessages(senderId, receiverId));
        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("receiverName", receiverName); // Receiver nomini modelga qo'shish
        model.addAttribute("receiver", receiver); // Receiver ma'lumotlari
        return "message";
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestParam String receiverName) { // Add receiverName as a parameter
        messageService.sendMessage(senderId, receiverId, content);
        return "redirect:/chat/messages/" + senderId + "/" + receiverId + "?receiverName=" + receiverName;
    }

    @PostMapping("/delete/{userId}/{chatPartnerId}")
    public String deleteChat(@PathVariable Long userId, @PathVariable Long chatPartnerId) {
        messageService.deleteChat(userId, chatPartnerId);
        return "redirect:/chat/list/" + userId;
    }

    @GetMapping("/search")
    public String searchChats(@RequestParam String username, @RequestParam Long userId, Model model) {
        List<User> users = userService.searchUsersByUsername(username, userId);
        List<Map<String, Object>> chats = users.stream().map(user -> {
            Map<String, Object> chat = new HashMap<>();
            chat.put("chatPartnerId", user.getId());
            chat.put("chatPartnerName", user.getUsername());
            chat.put("lastMessage", "");
            chat.put("lastMessageTime", "");
            chat.put("unreadCount", 0);
            chat.put("isExistingChat", false);
            return chat;
        }).collect(Collectors.toList());
        model.addAttribute("chats", chats);
        model.addAttribute("userId", userId);
        model.addAttribute("isSearchResult", true);
        return "chat_list";
    }

    @GetMapping("/search-ajax")
    @ResponseBody
    public List<Map<String, Object>> searchChatsAjax(@RequestParam String username, @RequestParam Long userId) {
        List<User> users = userService.searchUsersByUsername(username, userId);
        return users.stream().map(user -> {
            Map<String, Object> chat = new HashMap<>();
            chat.put("chatPartnerId", user.getId());
            chat.put("chatPartnerName", user.getUsername());
            chat.put("lastMessage", "");
            chat.put("lastMessageTime", "");
            chat.put("unreadCount", 0);
            chat.put("isExistingChat", false);
            return chat;
        }).collect(Collectors.toList());
    }
}
