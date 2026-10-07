package ru.newrecon.profile_service.kafka.consumer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.kafka.common.errors.SerializationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.profile_service.entity.InboxMessage;
import ru.newrecon.profile_service.kafka.payload.CreateEventPayload;
import ru.newrecon.profile_service.kafka.payload.CreateUserPayload;
import ru.newrecon.profile_service.repository.InboxMessageRepository;
import ru.newrecon.profile_service.service.ProfileEventService;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileEventConsumer {

    private final ProfileEventService profileEventService;
    private final InboxMessageRepository inboxMessageRepository;
    private final ObjectMapper kafkObjectMapper;

    @RetryableTopic(attempts = "3", 
        backOff = @BackOff(delay = 2000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
        exclude = {
            IllegalArgumentException.class,
            NullPointerException.class,
            JacksonException.class,
            SerializationException.class,
            DeserializationException.class,
            ClassCastException.class,
            IllegalStateException.class
        })
    @KafkaListener(topics = "create-event-events")
    public void listenCreateUser(
            String message,
            Acknowledgment ack,
            @Header(name = "idempotency-key", required = false) byte[] idempotencyKeyBytes
    ) {
        log.info("Получено сообщение из create-event-events: " + message);

        if (idempotencyKeyBytes == null || idempotencyKeyBytes.length == 0) {
            throw new IllegalArgumentException("Отсутствует заголовок idempotency-key, сообщение уходит в DLT");
        }

        String idempotencyKey = new String(idempotencyKeyBytes, StandardCharsets.UTF_8);
        InboxMessage inboxMessage = new InboxMessage();
        inboxMessage.setIdempotencyKey(UUID.fromString(idempotencyKey));
        inboxMessage.setPayload(message);

        try {
            inboxMessageRepository.saveAndFlush(inboxMessage);
        } catch (DataIntegrityViolationException e) {
            log.info("Дубликат с ключом идемпотентности: " + idempotencyKey);
            ack.acknowledge();
            return;
        }

        CreateEventPayload createEventPayload = kafkObjectMapper.readValue(message, CreateEventPayload.class);
        profileEventService.create(createEventPayload);
    }

    @DltHandler
    public void handleDlt(String message) {
        log.error("Сообщение попало в топик DLT: " + message);
    }
}
