// src/main/java/com/example/demo/controller/ProfileController.java
package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.SubscriptionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;
    @GetMapping("/profile/user/{userId}")
    public String showAllUsers(@PathVariable Long userId, Model model) {
        List<User> users = userService.findAllUsersExceptCurrent(userId);
        User currentUser = userService.findById(userId);
        model.addAttribute("users", users);
        model.addAttribute("currentUserId", userId);
        model.addAttribute("currentUsername", currentUser.getUsername());
        return "profile";
    }
    @GetMapping("/{userId}")
    public String showProfile(@PathVariable Long userId, Model model) {
        User currentUser = userService.getCurrentUser(userId);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUsername", currentUser.getUsername());
        return "profile";
    }
    @PostMapping("/subscribe")
    public String subscribe(@RequestParam Long userId, @RequestParam Long currentUserId) {
        User subscriber = userService.findById(currentUserId);
        User subscribedTo = userService.findById(userId);
        subscriptionService.subscribe(subscriber, subscribedTo);
        return "redirect:/profile/user/" + currentUserId;
    }
    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam Long userId, @RequestParam Long currentUserId) {
        User subscriber = userService.findById(currentUserId);
        User subscribedTo = userService.findById(userId);
        subscriptionService.unsubscribe(subscriber, subscribedTo);
        return "redirect:/profile/user/" + currentUserId;
    }
}
