package ru.newrecon.subscription_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.subscription_service.entity.Subscription;
import ru.newrecon.subscription_service.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription getById(UUID id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найдена подписка с id : " + id));
    }

    public Subscription save(Subscription Subscription) {
        return subscriptionRepository.save(Subscription);
    }

    public void delete(UUID id) {
        subscriptionRepository.deleteById(id);
    }
}
