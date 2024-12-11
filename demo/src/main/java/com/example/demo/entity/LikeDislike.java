package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
public class LikeDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public void setReactionType(ReactionType reactionType) {
    }

    public void setMenu(Menu menu) {
    }

    public void setUser(User user) {
    }
    public enum ReactionType {
        LIKE, DISLIKE
    }
}
