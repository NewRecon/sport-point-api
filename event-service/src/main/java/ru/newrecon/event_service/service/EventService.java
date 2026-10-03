package ru.newrecon.event_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.entity.enums.EventStatus;
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

    public List<Event> findAllActive() {
        return eventRepository.findAllByStatus(EventStatus.ACTIVE);
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

        currentEvent.setStatus(EventStatus.DELETED);
        eventRepository.save(currentEvent);

        eventSendService.sendDelete(currentEvent);
    }

    public int deleteExpired() {
        List<Event> expiredEvents = eventRepository.findAllExpired(LocalDateTime.now());

        expiredEvents.stream()
            .forEach(event -> event.setStatus(EventStatus.DELETED));

        return eventRepository.saveAll(expiredEvents).size();
    }
}
