package ru.newrecon.event_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.event_service.entity.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
