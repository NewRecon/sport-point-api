package ru.newrecon.subscription_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.subscription_service.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByEventId(UUID eventId);
}
