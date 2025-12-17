package com.example.demo.service;

import com.example.demo.model.Subscription;
import com.example.demo.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription subscribe(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public boolean unSubscribe(Subscription subscription) {
        subscriptionRepository.delete(subscription);
        return true;
    }
}
