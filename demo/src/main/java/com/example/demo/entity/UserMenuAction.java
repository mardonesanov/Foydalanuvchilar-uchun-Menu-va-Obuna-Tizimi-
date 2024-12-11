package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserMenuAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu; // Menyu

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    public enum ActionType {
        LIKE,
        DISLIKE
    }
}
