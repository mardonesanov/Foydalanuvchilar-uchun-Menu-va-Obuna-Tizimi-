package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId(Long userId);

    List<Comment> findByMenuId(Long menuId);
    List<Comment> findByMenu(Menu menu);
    List<Comment> findCommentsByMenu_UserId(Long userId);
}
