package ru.newrecon.event_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.enums.EventCategory;
import ru.newrecon.event_service.enums.EventStatus;
import ru.newrecon.event_service.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    // TODO воняет - вынести отправку в отдельный слой
    private final EventOutboxFacade eventOutboxFacade;

    public Event getById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найден ивент с id : " + id));
    }

    public List<Event> findAllActiveWithFilters(EventCategory category, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return eventRepository.findAllByStatusAndFilters(EventStatus.ACTIVE, category, dateFrom, dateTo);
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

        eventOutboxFacade.saveDeleteEventEvent(currentEvent);
    }

    public int deleteExpired() {
        List<Event> expiredEvents = eventRepository.findAllExpired(LocalDateTime.now());

        expiredEvents.stream()
            .forEach(event -> event.setStatus(EventStatus.DELETED));

        return eventRepository.saveAll(expiredEvents).size();
    }
}
