package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/message")
public class MessageController {
@Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @GetMapping("/{senderId}/{receiverId}")
    public String showChat(@PathVariable Long senderId, @PathVariable Long receiverId, Model model) {
        User receiver = userService.findById(receiverId);
        String receiverName = receiver.getUsername();
        model.addAttribute("messages", messageService.getMessages(senderId, receiverId)); // Xabarlarni olish
        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("receiverName", receiverName);
        return "message";
    }
    @PostMapping("/send")
    public String sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty!");
        }
        messageService.sendMessage(senderId, receiverId, content);
        return "redirect:/message/" + senderId + "/" + receiverId;
    }
}