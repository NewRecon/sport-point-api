package ru.newrecon.event_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event getById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найден ивент с id : " + id));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(UUID id) {
        eventRepository.deleteById(id);
    }
}
