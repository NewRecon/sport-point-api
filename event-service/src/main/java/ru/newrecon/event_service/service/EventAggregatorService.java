package ru.newrecon.event_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.event_service.dto.GetEventRs;
import ru.newrecon.event_service.entity.Event;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventAggregatorService {

    private final EventService eventService;
    private final SubscriptionService subscriptionService;
 
    public GetEventRs getEventPageData(UUID eventId) {
        Event event = eventService.getById(eventId);
        int currentParticipantsCount = subscriptionService.getUserCountByIventId(eventId);

        return buildGetEventRs(event, currentParticipantsCount);
    }

    private GetEventRs buildGetEventRs(Event event, int currentParticipantsCount) {
        return new GetEventRs(
            event.getId(),
            event.getTitle(),
            event.getLocationName(),
            event.getLatitude(),
            event.getLongitude(),
            event.getDescription(),
            event.getDate(),
            event.getOwnerId(),
            event.getTotalParticipants(),
            currentParticipantsCount
        );
    }
}
