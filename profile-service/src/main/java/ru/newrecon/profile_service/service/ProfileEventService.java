package ru.newrecon.profile_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.entity.ProfileEvent;
import ru.newrecon.profile_service.kafka.payload.CreateEventPayload;
import ru.newrecon.profile_service.kafka.payload.SubscribeSubscriptionPayload;
import ru.newrecon.profile_service.repository.ProfileEventRepository;

@Service 
@RequiredArgsConstructor 
public class ProfileEventService {

    private final ProfileEventRepository profileEventRepository;

    public void create(CreateEventPayload createEventPayload) {
        ProfileEvent profileEvent = new ProfileEvent();
        profileEvent.setEventId(createEventPayload.eventId()); 
        profileEvent.setEventTitle(createEventPayload.eventTitle()); 
        profileEvent.setUserId(createEventPayload.userId()); 
        profileEvent.setOwner(true);
        profileEvent.setCreatorParticipant(createEventPayload.isCreatorParticipant());

        profileEventRepository.save(profileEvent);
    }

    public void create(SubscribeSubscriptionPayload subscribeSubscriptionPayload) {
        ProfileEvent profileEvent = new ProfileEvent();
        profileEvent.setEventId(subscribeSubscriptionPayload.eventId()); 
        profileEvent.setEventTitle(subscribeSubscriptionPayload.eventTitle()); 
        profileEvent.setUserId(subscribeSubscriptionPayload.userId()); 
        profileEvent.setOwner(false);
        profileEvent.setCreatorParticipant(false);

        profileEventRepository.save(profileEvent);
    }

    public List<ProfileEvent> findByUserId(UUID userId) {
        return profileEventRepository.findByUserId(userId);
    }
}
