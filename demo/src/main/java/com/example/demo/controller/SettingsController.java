package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class SettingsController {
    @Autowired
    private UserService userService;
    @GetMapping("/settings")
    public String settingsPage(@RequestParam Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "settings";
    }
    @PostMapping("/settings")
    public String updateUserInfo(@RequestParam Long userId,
                                 @RequestParam String username,
                                 @RequestParam String phoneNumber) {
        User user = userService.findById(userId);
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        userService.signUp(user);

        return "redirect:/settings?userId=" + userId;
    }
    @PostMapping("/update_role")
    public String updateRole(@RequestParam Long userId, @RequestParam String role) {
        userService.updateUserRole(userId, role);
        return "redirect:/personal/user/" + userId;
    }
    @GetMapping("/settings_content")
    public String settingsContentPage(@RequestParam Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "settings_content";
    }
    @PostMapping("/settings/upload_image")
    public String uploadProfileImage(@RequestParam Long userId,
                                     @RequestParam("profileImage") MultipartFile profileImage) {
        try {
            userService.uploadProfileImage(userId, profileImage);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload profile image", e);
        }
        return "redirect:/settings_content?userId=" + userId;
    }

}
