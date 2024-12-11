package com.example.demo.service;

import com.example.demo.entity.Menu;
import com.example.demo.entity.SavedMenu;
import com.example.demo.entity.User;
import com.example.demo.repository.SavedMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedMenuService {

    @Autowired
    private SavedMenuRepository savedMenuRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    public void saveMenuForUser(Long userId, Long menuId) {
        var existing = savedMenuRepository.findByUserIdAndMenuId(userId, menuId);
        if (existing.isPresent()) {
            return;
        }
        User user = userService.findById(userId);
        Menu menu = menuService.findMenuById(menuId);
        SavedMenu savedMenu = new SavedMenu();
        savedMenu.setUser(user);
        savedMenu.setMenu(menu);

        savedMenuRepository.save(savedMenu);
    }

    public List<SavedMenu> getSavedMenusForUser(Long userId) {
        return savedMenuRepository.findByUserId(userId);
    }
    public void removeSavedMenu(Long savedMenuId) {
        savedMenuRepository.deleteById(savedMenuId);
    }
}
