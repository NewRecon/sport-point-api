package ru.newrecon.subscription_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import ru.newrecon.subscription_service.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Modifying
    @Transactional
    @Query("""
            UPDATE Subscription s 
            SET s.status = ru.newrecon.subscription_service.entity.enums.Status.DELETED
            WHERE s.eventId = :eventId
            """)
    int deleteByEventId(UUID eventId);

    Optional<Subscription> findByEventIdAndUserId(UUID eventId, UUID userId);
}
