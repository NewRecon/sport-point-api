package ru.newrecon.event_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.enums.EventStatus;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByStatus(EventStatus status);

    @Query (value = """
            select e
            from Event e
            where e.date < :currenDateTime
            """)
    List<Event> findAllExpired(LocalDateTime currenDateTime);
}
