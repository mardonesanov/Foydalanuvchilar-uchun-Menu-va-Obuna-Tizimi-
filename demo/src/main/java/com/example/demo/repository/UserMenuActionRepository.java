package com.example.demo.repository;

import com.example.demo.entity.UserMenuAction;
import com.example.demo.entity.User;
import com.example.demo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMenuActionRepository extends JpaRepository<UserMenuAction, Long> {
    Optional<UserMenuAction> findByUserAndMenu(User user, Menu menu);
}
