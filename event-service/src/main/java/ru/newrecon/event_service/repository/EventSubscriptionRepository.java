package ru.newrecon.event_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.event_service.entity.EventSubscription;

public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, UUID> {
    List<EventSubscription> findAllByEventId(UUID eventId);
}
