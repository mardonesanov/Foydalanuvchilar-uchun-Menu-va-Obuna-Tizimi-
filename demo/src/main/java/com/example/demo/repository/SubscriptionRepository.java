package com.example.demo.repository;

import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findBySubscriberAndSubscribedTo(User subscriber, User subscribedTo);
    void deleteBySubscriberAndSubscribedTo(User subscriber, User subscribedTo);
    void deleteBySubscribedTo(User subscribedTo);  // Make sure this method exists
    List<Subscription> findBySubscriberOrderBySubscribedAtDesc(User subscriber);

    void deleteBySubscriber(User user);
}
