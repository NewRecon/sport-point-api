package ru.newrecon.event_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.kafka.payload.CreateEventPayload;
import ru.newrecon.event_service.kafka.payload.DeleteEventPayload;
import ru.newrecon.event_service.kafka.producer.EventProducer;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class EventSendService {

    private final EventProducer eventProducer;
    private final ObjectMapper kafkObjectMapper;

    public void sendCreate(Event event) {
        CreateEventPayload createEventDto = new CreateEventPayload(
            event.getId(), event.getOwnerId(), event.getTotalParticipants()
        );

        String kafkaMessage = kafkObjectMapper.writeValueAsString(createEventDto);
        eventProducer.sendCreate(kafkaMessage);
    }

    public void sendDelete(Event event) {
        DeleteEventPayload deleteEventDto = new DeleteEventPayload(event.getId());

        String kafkaMessage = kafkObjectMapper.writeValueAsString(deleteEventDto);
        eventProducer.sendDelete(kafkaMessage);
    }
}
