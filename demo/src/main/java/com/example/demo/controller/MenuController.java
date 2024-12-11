package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    // Personal page for the user, shows menus and role
    @GetMapping("/personal/user/{userId}")
    public String personalPage(@PathVariable Long userId, Model model) {
        List<Menu> userMenus = menuService.findAllMenusByUserId(userId);
        User user = userService.findById(userId); // Fetch the user by ID
        model.addAttribute("userMenus", userMenus);
        model.addAttribute("userId", userId);
        model.addAttribute("role", user.getRole()); // Add the role to the model
        return "personal";
    }

    // Show form to update a menu item
    @GetMapping("/update_menu")
    public String showUpdateForm(@RequestParam Long menuId, Model model) {
        Menu menu = menuService.findMenuById(menuId);
        model.addAttribute("menu", menu);
        return "update_menu";
    }

    // Handle the menu update
    @PostMapping("/update_menu")
    public String updateMenu(@ModelAttribute Menu menu) {
        if (menu.getUser() == null || menu.getUser().getId() == null) {
            throw new RuntimeException("Foydalanuvchi ma'lumotlari topilmadi!"); // Handle missing user
        }
        menuService.updateMenu(menu); // Update the menu
        return "redirect:/personal/user/" + menu.getUser().getId(); // Redirect to the personal page
    }

    // Delete menu item and redirect to the personal page
    @PostMapping("/delete_menu")
    public String deleteMenu(@RequestParam Long menuId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Deleting menu with ID: {} for user ID: {}", menuId, userId);
            menuService.deleteMenu(menuId); // Delete the menu item
            redirectAttributes.addFlashAttribute("successMessage", "Menyu muvaffaqiyatli o'chirildi!"); // Success message
        } catch (Exception e) {
            logger.error("Error deleting menu with ID: {} for user ID: {}", menuId, userId, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Xatolik yuz berdi, iltimos qaytadan urinib ko'ring."); // Error message
        }
        return "redirect:/personal/user/" + userId; // Redirect to the personal page
    }
}
