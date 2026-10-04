package ru.newrecon.subscription_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.subscription_service.entity.Subscription;
import ru.newrecon.subscription_service.enums.ParticipantRole;
import ru.newrecon.subscription_service.enums.SubscriptionStatus;
import ru.newrecon.subscription_service.exception.NoMorePlacesException;
import ru.newrecon.subscription_service.exception.UserAlreadySubscribeException;
import ru.newrecon.subscription_service.kafka.payload.CreateEventPayload;
import ru.newrecon.subscription_service.repository.SubscriptionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionSendService subscriptionSendService;
    private final CounterService counterService;

    public Subscription getById(UUID id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найдена подписка с id : " + id));
    }

    // TODO воняет
    @Transactional
    public void create(CreateEventPayload createEventDto) {
        Subscription subscription = new Subscription();
        subscription.setUserId(createEventDto.userId());
        subscription.setEventId(createEventDto.eventId());
        subscription.setCreateAt(LocalDateTime.now());
        subscription.setParticipantRole(ParticipantRole.OWNER);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        counterService.setCounterValue(createEventDto.eventId().toString(), createEventDto.totalParticipants()-1);

        subscriptionRepository.save(subscription);
    }

    public Subscription save(Subscription Subscription) {
        return subscriptionRepository.save(Subscription);
    }

    public void delete(UUID id) {
        subscriptionRepository.deleteById(id);
    }

    @Transactional
    public void subscribe(UUID userId, UUID eventId, String username) {
        if (subscriptionRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new UserAlreadySubscribeException("Пользователь уже записан на ивент " + userId);
        }

        long subsCount = counterService.decrement(eventId.toString());

        if (subsCount < 0) {
            throw new NoMorePlacesException("Мест на ивент больше нет " + eventId);
        }

        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setEventId(eventId);
        subscription.setCreateAt(LocalDateTime.now());
        subscription.setParticipantRole(ParticipantRole.MEMBER);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        subscriptionRepository.save(subscription);

        subscriptionSendService.sendSubscribe(subscription, username);
    }

    @Transactional
    public void unsubscribe(UUID userId, UUID eventId) {
       Subscription currentSubscription = subscriptionRepository.findByEventIdAndUserId(eventId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Не найдена подписка с eventId : " + eventId));

        currentSubscription.setStatus(SubscriptionStatus.DELETED);
        subscriptionRepository.save(currentSubscription);

        counterService.increment(eventId.toString());
    }

    @Transactional
    public void deleteByEventId(UUID eventId) {
        int deleteByEventId = subscriptionRepository.deleteByEventId(eventId);
        log.info("При удалении ивента с id : " + eventId + "было удалено подписок : " + deleteByEventId);

        counterService.deleteCounter(eventId.toString());
    }
}
