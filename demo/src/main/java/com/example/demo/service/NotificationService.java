package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               MenuRepository menuRepository,
                               CommentRepository commentRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.commentRepository = commentRepository;
    }
    public void addNotification(Long userId, String content, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setType(type);
        notification.setUser(user);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }
    public List<Notification> getNotificationsForUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    public void deleteAllNotifications() {
        notificationRepository.deleteAll();
    }

    public List<Comment> getCommentsForUserMenus(Long userId) {
        List<Menu> userMenus = menuRepository.findByUserId(userId);
        List<Comment> allComments = new ArrayList<>();
        for (Menu menu : userMenus) {
            allComments.addAll(commentRepository.findByMenuId(menu.getId()));
        }
        return allComments;
    }
    public void deleteUserNotifications(Long userId) {
        List<Notification> userNotifications = notificationRepository.findByUserId(userId);
        notificationRepository.deleteAll(userNotifications);
    }


    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

}
