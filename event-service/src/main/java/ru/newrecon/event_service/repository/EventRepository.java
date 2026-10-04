package ru.newrecon.event_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.enums.EventCategory;
import ru.newrecon.event_service.enums.EventStatus;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query(value = """
            SELECT e FROM Event e
            WHERE e.status = :status
            AND (:category IS NULL OR e.category = :category)
            AND (cast(:dateFrom as localdatetime) IS NULL OR e.date >= :dateFrom)
            AND (cast(:dateTo as localdatetime) IS NULL OR e.date <= :dateTo)
           """)
    List<Event> findAllByStatusAndFilters(
        EventStatus status, EventCategory category, LocalDateTime dateFrom, LocalDateTime dateTo
    );

    @Query (value = """
            select e
            from Event e
            where e.date < :currenDateTime
            """)
    List<Event> findAllExpired(LocalDateTime currenDateTime);
}
