package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.SavedMenu;
import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.SubscriptionService;
import com.example.demo.service.UserService;
import com.example.demo.service.SavedMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SavedMenuService savedMenuService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user) {
        userService.signUp(user);
        return "redirect:/signin";
    }
    @GetMapping("/signin")
    public String signinForm() {
        return "signin";
    }
    @PostMapping("/signin")
    public String signinSubmit(@RequestParam String username,
                               @RequestParam String phoneNumber, Model model) {
        var user = userService.findByUsernameAndPhoneNumber(username, phoneNumber);
        if (user.isPresent()) {
            if ("ADMIN".equals(user.get().getRole())) {
                return "redirect:/admin/users";
            }
            model.addAttribute("userId", user.get().getId());
            return "redirect:/user/tizim?userId=" + user.get().getId();
        }
        model.addAttribute("error", "Foydalanuvchi topilmadi!");
        return "signin";
    }
    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("searchQuery", "");
        return "tizim";
    }
    @GetMapping("/user/tizim")
    public String tizimPage(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "search", required = false) String searchQuery,
            Model model) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID bo'lishi shart!");
        }
        User user = userService.findById(userId);

        List<Menu> currentUserMenus;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            currentUserMenus = menuService.searchMenusByUserIdAndQuery(userId, searchQuery);
        } else {
            currentUserMenus = menuService.findAllMenusByUserId(userId);
        }

        List<Subscription> subscriptions = subscriptionService.findSubscriptionsForUser(user);
        List<SubscribedUserMenus> subscribedUserMenusList = subscriptions.stream().map(sub -> {
            List<Menu> subMenus = menuService.findAllMenusByUserId(sub.getSubscribedTo().getId());
            return new SubscribedUserMenus(sub.getSubscribedTo(), subMenus);
        }).toList();
        model.addAttribute("user", user);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("currentUserMenus", currentUserMenus);
        model.addAttribute("subscribedUserMenusList", subscribedUserMenusList);
        return "tizim";
    }
    @GetMapping("/add_menu")
    public String addMenuForm(@RequestParam Long userId, Model model) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("Foydalanuvchi topilmadi!");
        }
        model.addAttribute("userId", userId);
        model.addAttribute("menu", new Menu());
        return "add_menu";
    }
    @PostMapping("/add_menu")
    public String addMenuSubmit(Menu menu, @RequestParam Long userId) {
        menuService.saveMenu(menu, userId);
        return "redirect:/user/tizim?userId=" + userId;
    }
    @GetMapping("/saved")
    public String showSaved(@RequestParam Long userId, Model model) {
        User user = userService.findById(userId);
        List<SavedMenu> savedMenus = savedMenuService.getSavedMenusForUser(userId);
        model.addAttribute("user", user);
        model.addAttribute("savedMenus", savedMenus);
        return "saved";
    }
    @PostMapping("/save_menu")
    public String saveMenu(@RequestParam Long userId, @RequestParam Long menuId) {
        savedMenuService.saveMenuForUser(userId, menuId);
        return "redirect:/user/tizim?userId=" + userId;
    }
    @GetMapping("/download_menu")
    public ResponseEntity<byte[]> downloadMenu(@RequestParam Long menuId) {
        Menu menu = menuService.findMenuById(menuId);
        String fileContent = "Product: " + menu.getProductName() + "\n" +
                "Price: " + menu.getPrice() + "\n" +
                "Category: " + menu.getCategory();
        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=menu_" + menu.getId() + ".txt");
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/remove_saved_menu")
    public String removeSavedMenu(@RequestParam Long userId, @RequestParam Long savedMenuId) {
        savedMenuService.removeSavedMenu(savedMenuId);
        return "redirect:/saved?userId=" + userId;
    }

    class SubscribedUserMenus {
        private User subscribedUser;
        private List<Menu> menus;

        public SubscribedUserMenus(User subscribedUser, List<Menu> menus) {
            this.subscribedUser = subscribedUser;
            this.menus = menus;
        }

        public User getSubscribedUser() {
            return subscribedUser;
        }

        public List<Menu> getMenus() {
            return menus;
        }
    }
}
