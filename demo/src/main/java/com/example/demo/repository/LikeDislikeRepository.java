package com.example.demo.repository;

import com.example.demo.entity.LikeDislike;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {
    List<LikeDislike> findByMenu(Menu menu);

    LikeDislike findByMenuAndUser(Menu menu, User user);
}
