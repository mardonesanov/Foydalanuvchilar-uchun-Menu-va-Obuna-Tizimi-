package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private User subscriber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribed_to_id")
    private User subscribedTo;
    private LocalDateTime subscribedAt;
    private LocalDateTime unsubscribedAt;
    private boolean isActive = true;
}
