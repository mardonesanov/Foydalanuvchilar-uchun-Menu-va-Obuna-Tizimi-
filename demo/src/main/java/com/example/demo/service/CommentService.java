package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByMenu(Menu menu) {
        return commentRepository.findByMenu(menu);
    }

    public void addComment(User user, Menu menu, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now().toString());
        comment.setUser(user);
        comment.setMenu(menu);
        commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Izoh topilmadi"));
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsForUserMenus(Long userId) {
        return commentRepository.findCommentsByMenu_UserId(userId);
    }

    public List<Comment> getCommentsForMenu(Long menuId) {
        return commentRepository.findByMenuId(menuId);
    }
}
