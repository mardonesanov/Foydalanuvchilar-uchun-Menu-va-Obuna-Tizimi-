package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Notification;
import com.example.demo.service.CommentService;
import com.example.demo.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NotificationController {

    private final CommentService commentService;
    private final NotificationService notificationService;
    public NotificationController(CommentService commentService, NotificationService notificationService) {
        this.commentService = commentService;
        this.notificationService = notificationService;
    }
    @GetMapping("/notifications/{userId}")
    public String showNotifications(@PathVariable Long userId, Model model) {
        List<Comment> userMenuComments = commentService.getCommentsForUserMenus(userId);
        List<Notification> userNotifications = notificationService.getNotificationsForUserById(userId);

        model.addAttribute("comments", userMenuComments);
        model.addAttribute("notifications", userNotifications);
        return "notification";
    }
    @GetMapping("/notifications/clear")
    public String clearAllNotifications() {
        notificationService.deleteAllNotifications();
        return "redirect:/notifications";
    }
    @PostMapping("/notifications/add")
    public String addNotification(@RequestParam Long userId,
                                  @RequestParam String content,
                                  @RequestParam String type) {
        notificationService.addNotification(userId, content, type);
        return "redirect:/notifications/" + userId;
    }

    @GetMapping("/comments/menu/{menuId}")
    public String showCommentsForMenu(@PathVariable Long menuId, Model model) {
        List<Comment> menuComments = commentService.getCommentsForMenu(menuId);
        model.addAttribute("comments", menuComments);
        return "menu-comments";
    }
    @GetMapping("/notifications/clear/{userId}")
    public String clearUserNotifications(@PathVariable Long userId) {
        List<Notification> userNotifications = notificationService.getNotificationsForUserById(userId);
        for (Notification notification : userNotifications) {
            notificationService.deleteNotification(notification.getId());
        }
        return "redirect:/notifications/" + userId;
    }
}
