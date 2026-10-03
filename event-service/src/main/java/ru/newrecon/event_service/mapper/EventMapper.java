package ru.newrecon.event_service.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.newrecon.event_service.config.MapstructConfig;
import ru.newrecon.event_service.dto.CreateEventRq;
import ru.newrecon.event_service.dto.CreateEventRs;
import ru.newrecon.event_service.dto.EventViewDto;
import ru.newrecon.event_service.dto.GetEventRs;
import ru.newrecon.event_service.dto.GetViewEventRs;
import ru.newrecon.event_service.dto.UpdateEventRq;
import ru.newrecon.event_service.dto.UpdateEventRs;
import ru.newrecon.event_service.entity.Event;

@Mapper(config = MapstructConfig.class)
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Event map(UUID ownerId, CreateEventRq source);

    Event map(UUID ownerId, UUID id, UpdateEventRq source);

    CreateEventRs mapToCreateEventRs(Event source);

    UpdateEventRs mapToUpdateEventRs(Event source);

    GetViewEventRs mapToGetViewEventRs(EventViewDto source);
    
    List<GetEventRs> mapToGetEventRs(List<Event> source);
}
