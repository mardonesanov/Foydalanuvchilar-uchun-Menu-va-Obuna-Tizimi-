package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/information")
public class InformationController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @GetMapping("/{userId}")
    public String showUserProfile(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId);
        List<Menu> menus = menuService.findByUserId(userId); // Get user's menus

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);

        return "information";
    }
    @PostMapping("/like/{menuId}")
    public String likeMenu(@PathVariable Long menuId, @RequestParam Long userId) {
        User user = userService.findById(userId);
        menuService.toggleLikeOrDislike(menuId, user, true); // like = true
        return "redirect:/information/" + userId;
    }
    @PostMapping("/dislike/{menuId}")
    public String dislikeMenu(@PathVariable Long menuId, @RequestParam Long userId) {
        User user = userService.findById(userId);
        menuService.toggleLikeOrDislike(menuId, user, false);
        return "redirect:/information/" + userId;
    }
}
