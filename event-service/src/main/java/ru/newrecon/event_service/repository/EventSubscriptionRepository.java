package ru.newrecon.event_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.newrecon.event_service.entity.EventSubscription;

public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, UUID> {

    @Query(value = """
            select count(s)
            from EventSubscription s
            where s.eventId = :eventId
            """)
    int getUserCountByEventId(UUID eventId);
}
