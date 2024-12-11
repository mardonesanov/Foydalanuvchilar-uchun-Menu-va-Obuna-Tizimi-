package com.example.demo.service;

import com.example.demo.entity.Menu;
import com.example.demo.entity.SavedMenu;
import com.example.demo.entity.User;
import com.example.demo.entity.UserMenuAction;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.SavedMenuRepository;
import com.example.demo.repository.UserMenuActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserMenuActionRepository userMenuActionRepository;

    @Autowired
    private SavedMenuRepository savedMenuRepository;

    @Autowired
    private UserService userService;

    public List<Menu> findAllMenusByUserId(Long userId) {
        return menuRepository.findByUserId(userId);
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + menuId));
    }

    public List<Menu> findByUserId(Long userId) {
        return menuRepository.findByUserId(userId);
    }

    public void toggleLikeOrDislike(Long menuId, User user, boolean isLike) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        Optional<UserMenuAction> existingAction = userMenuActionRepository.findByUserAndMenu(user, menu);

        if (existingAction.isPresent()) {
            UserMenuAction action = existingAction.get();

            if (action.getActionType() == (isLike ? UserMenuAction.ActionType.LIKE : UserMenuAction.ActionType.DISLIKE)) {
                // If the user has already performed the same action, remove it
                userMenuActionRepository.delete(action);
                if (isLike) {
                    menu.setLikes(menu.getLikes() - 1); // Decrease likes
                } else {
                    menu.setDislikes(menu.getDislikes() - 1); // Decrease dislikes
                }
            } else {
                // If the user performed a different action, update it
                action.setActionType(isLike ? UserMenuAction.ActionType.LIKE : UserMenuAction.ActionType.DISLIKE);
                userMenuActionRepository.save(action);

                // Update like and dislike counts
                if (isLike) {
                    menu.setLikes(menu.getLikes() + 1);
                    menu.setDislikes(menu.getDislikes() - 1); // Decrease dislikes
                } else {
                    menu.setDislikes(menu.getDislikes() + 1);
                    menu.setLikes(menu.getLikes() - 1); // Decrease likes
                }
            }
        } else {
            // If no action exists, create a new one
            UserMenuAction newAction = new UserMenuAction();
            newAction.setUser(user);
            newAction.setMenu(menu);
            newAction.setActionType(isLike ? UserMenuAction.ActionType.LIKE : UserMenuAction.ActionType.DISLIKE);
            userMenuActionRepository.save(newAction);

            // Update like or dislike count
            if (isLike) {
                menu.setLikes(menu.getLikes() + 1);
            } else {
                menu.setDislikes(menu.getDislikes() + 1);
            }
        }

        menuRepository.save(menu); // Save updated menu
    }



    public Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found!"));
    }

    public void saveMenu(Menu menu, Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }
        menu.setUser(user);
        menuRepository.save(menu);
    }

    public void updateMenu(Menu menu) {
        menuRepository.save(menu);
    }

    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with ID: " + menuId));
        savedMenuRepository.deleteByMenu(menu);
        menuRepository.delete(menu);
    }

    public void repostMenu(Long userId, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found!"));

        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        SavedMenu savedMenu = new SavedMenu();
        savedMenu.setUser(user);
        savedMenu.setMenu(menu);

        savedMenuRepository.save(savedMenu);
    }

    public List<Menu> searchMenusByUserIdAndQuery(Long userId, String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            // Return all menus if no search query is provided
            return menuRepository.findAllByUserId(userId);
        }
        // Perform a case-insensitive search for menus
        return menuRepository.searchMenusByUserIdAndQuery(userId, searchQuery.trim());
    }

}
