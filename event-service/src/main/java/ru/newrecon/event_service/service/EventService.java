package ru.newrecon.event_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.entity.enums.Status;
import ru.newrecon.event_service.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventSendService eventSendService;

    public Event getById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найден ивент с id : " + id));
    }

    @Transactional
    public Event create(Event event) {
        event.setStatus(Status.ACTIVE);
        Event currentEvent = eventRepository.save(event);

        eventSendService.sendCreate(currentEvent);
        
        return currentEvent;
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(UUID id) {
        eventRepository.deleteById(id);
    }

    @Transactional
    public void delete(UUID id) {
        Event currentEvent = getById(id);

        currentEvent.setStatus(Status.DELETED);
        eventRepository.save(currentEvent);

        eventSendService.sendDelete(currentEvent);
    }
}
