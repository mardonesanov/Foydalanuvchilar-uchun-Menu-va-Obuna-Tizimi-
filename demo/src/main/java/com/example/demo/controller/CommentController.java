package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;
    @GetMapping("/{menuId}")
    public String showComments(@PathVariable Long menuId, Model model, @RequestParam Long userId) {
        Menu menu = menuService.findById(menuId); // Menu ma'lumotlarini olish
        List<Comment> comments = commentService.getCommentsByMenu(menu); // Izohlarni olish
        User user = userService.findById(userId); // Foydalanuvchini olish

        model.addAttribute("menu", menu);
        model.addAttribute("comments", comments); // Izohlarni modelga qo'shish
        model.addAttribute("user", user); // Foydalanuvchi obyektini modelga qo'shish
        return "comment";
    }
    @PostMapping("/add/{menuId}")
    public String addComment(@PathVariable Long menuId, @RequestParam Long userId, @RequestParam String content) {
        User user = userService.findById(userId); // Foydalanuvchini olish
        Menu menu = menuService.findById(menuId); // Menu ma'lumotlarini olish
        commentService.addComment(user, menu, content);

        return "redirect:/comments/" + menuId + "?userId=" + userId; // Izoh qo'shishdan so'ng, izohlar sahifasiga qaytish
    }
    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long menuId, @RequestParam Long userId) {
        Comment comment = commentService.findById(commentId); // Izohni olish
        if (comment.getUser().getId().equals(userId)) { // Foydalanuvchi o'z izohini o'chirayotganini tekshirish
            commentService.deleteComment(commentId); // Izohni o'chirish
        }
        return "redirect:/comments/" + menuId + "?userId=" + userId; // Izohlar sahifasiga qaytish
    }

}
