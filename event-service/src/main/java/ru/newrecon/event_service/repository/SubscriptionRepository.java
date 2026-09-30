package ru.newrecon.event_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.newrecon.event_service.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Query(value = """
            select count(s)
            from Subscription s
            where s.eventId = :eventId
            """)
    int getUserCountByEventId(UUID eventId);
}
