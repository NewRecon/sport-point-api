package ru.newrecon.event_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.event_service.dto.EventViewDto;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.entity.EventSubscription;
import ru.newrecon.event_service.enums.EventStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventAggregatorService {

    private final EventService eventService;
    private final EventSubscriptionService eventSubscriptionService;
    private final EventOutboxFacade eventOutboxFacade;

    public EventViewDto getEventViewData(UUID eventId) {
        Event event = eventService.getById(eventId);
        List<EventSubscription> eventSubscriptions = eventSubscriptionService.findAllByEventId(eventId);

        return buildEventViewDto(event, eventSubscriptions);
    }

    @Transactional
    public Event createEvent(Event event, String username, boolean isCreatorParticipant) {
        event.setStatus(EventStatus.ACTIVE);
        Event currentEvent = eventService.save(event);

        if (isCreatorParticipant) {
            EventSubscription eventSubscription = new EventSubscription();
            eventSubscription.setUserId(event.getOwnerId());
            eventSubscription.setEventId(event.getId());
            eventSubscription.setUsername(username);
            eventSubscriptionService.save(eventSubscription);
        }

        eventOutboxFacade.saveCreateEventEvent(currentEvent, isCreatorParticipant);

        return currentEvent;
    }

    private EventViewDto buildEventViewDto(Event event, List<EventSubscription> eventSubscriptions) {
        return new EventViewDto(
                event.getId(),
                event.getTitle(),
                event.getLocationName(),
                event.getLatitude(),
                event.getLongitude(),
                event.getDescription(),
                event.getDate(),
                event.getOwnerId(),
                event.getTotalParticipants(),
                eventSubscriptions);
    }
}
