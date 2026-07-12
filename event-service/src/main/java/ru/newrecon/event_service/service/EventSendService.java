package ru.newrecon.event_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.dto.kafka.CreateEventDto;
import ru.newrecon.event_service.dto.kafka.DeleteEventDto;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.producer.EventProducer;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class EventSendService {

    private final EventProducer eventProducer;
    private final ObjectMapper kafkObjectMapper;

    public void sendCreate(Event event) {

        CreateEventDto createEventDto = new CreateEventDto(
            event.getId(), event.getOwnerId(), event.getTotalParticipants()
        );

        String kafkaMessage = kafkObjectMapper.writeValueAsString(createEventDto);
        eventProducer.sendCreate(kafkaMessage);
    }

    public void sendDelete(Event event) {

        DeleteEventDto deleteEventDto = new DeleteEventDto(event.getId());

        String kafkaMessage = kafkObjectMapper.writeValueAsString(deleteEventDto);
        eventProducer.sendDelete(kafkaMessage);
    }
}
