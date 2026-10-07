package ru.newrecon.event_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.EventSubscription;
import ru.newrecon.event_service.kafka.payload.SubscribeSubscriptionPayload;
import ru.newrecon.event_service.repository.EventSubscriptionRepository;

@Service
@RequiredArgsConstructor
public class EventSubscriptionService {

    private final EventSubscriptionRepository eventSubscriptionRepository;

    // TODO воняет
    public void create(SubscribeSubscriptionPayload subscribeSubscriptionDto) {
        EventSubscription eventSubscription = new EventSubscription();
        eventSubscription.setEventId(subscribeSubscriptionDto.eventId());
        eventSubscription.setUserId(subscribeSubscriptionDto.userId());
        eventSubscription.setName(subscribeSubscriptionDto.name());

        eventSubscriptionRepository.save(eventSubscription);
    }

    public EventSubscription save(EventSubscription eventSubscription) {
        return eventSubscriptionRepository.save(eventSubscription);
    }

    public List<EventSubscription> findAllByEventId(UUID eventId) {
        return eventSubscriptionRepository.findAllByEventId(eventId);
    }
}
