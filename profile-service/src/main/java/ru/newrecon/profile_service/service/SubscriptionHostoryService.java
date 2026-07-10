package ru.newrecon.profile_service.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.dto.kafka.CreateEventDto;
import ru.newrecon.profile_service.entity.SubscriptionHistory;
import ru.newrecon.profile_service.enums.ParticipantRole;
import ru.newrecon.profile_service.repository.SubscriptionHistoryRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionHostoryService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    public void create(CreateEventDto createEvent) {
        SubscriptionHistory subscriptionHistory = new SubscriptionHistory();

        subscriptionHistory.setEventId(createEvent.eventId());
        subscriptionHistory.setUserId(createEvent.userId());
        subscriptionHistory.setStartedAt(LocalDateTime.now());
        subscriptionHistory.setParticipantRole(ParticipantRole.OWNER);

        subscriptionHistoryRepository.save(subscriptionHistory);
    }
}
