package com.example.demo.repository;

import com.example.demo.entity.Menu;
import com.example.demo.entity.SavedMenu;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedMenuRepository extends JpaRepository<SavedMenu, Long> {
    Optional<SavedMenu> findByUserIdAndMenuId(Long userId, Long menuId);
    List<SavedMenu> findByUserId(Long userId);
    void deleteByMenu(Menu menu);

    void deleteByUser(User user);
}
