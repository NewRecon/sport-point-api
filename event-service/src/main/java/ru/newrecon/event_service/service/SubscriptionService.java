package ru.newrecon.event_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public int getUserCountByIventId(UUID eventId) {
        return subscriptionRepository.getUserCountByEventId(eventId);
    }
}
