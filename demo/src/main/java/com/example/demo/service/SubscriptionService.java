// src/main/java/com/example/demo/service/SubscriptionService.java
package com.example.demo.service;

import com.example.demo.entity.Subscription;
import com.example.demo.entity.User;
import com.example.demo.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public void subscribe(User subscriber, User subscribedTo) {
        Optional<Subscription> existing = subscriptionRepository.findBySubscriberAndSubscribedTo(subscriber, subscribedTo);
        if (existing.isEmpty()) {
            Subscription subscription = new Subscription();
            subscription.setSubscriber(subscriber);
            subscription.setSubscribedTo(subscribedTo);
            subscription.setSubscribedAt(LocalDateTime.now());
            subscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public void unsubscribe(User subscriber, User subscribedTo) {
        subscriptionRepository.deleteBySubscriberAndSubscribedTo(subscriber, subscribedTo);
    }

    public List<Subscription> findSubscriptionsForUser(User subscriber) {
        return subscriptionRepository.findBySubscriberOrderBySubscribedAtDesc(subscriber);
    }
}
