package ru.newrecon.event_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.dto.kafka.SubscribeSubscriptionDto;
import ru.newrecon.event_service.entity.EventSubscription;
import ru.newrecon.event_service.repository.EventSubscriptionRepository;

@Service
@RequiredArgsConstructor
public class EventSubscriptionService {

    private final EventSubscriptionRepository subscriptionRepository;

    public void create(SubscribeSubscriptionDto subscribeSubscriptionDto) {
        EventSubscription eventSubscription = new EventSubscription();
        eventSubscription.setEventId(subscribeSubscriptionDto.eventId());
        eventSubscription.setUserId(subscribeSubscriptionDto.userId());

        subscriptionRepository.save(eventSubscription);
    }

    public int getUserCountByIventId(UUID eventId) {
        return subscriptionRepository.getUserCountByEventId(eventId);
    }
}
