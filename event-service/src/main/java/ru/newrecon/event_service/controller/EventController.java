package ru.newrecon.event_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.dto.CreateEventRq;
import ru.newrecon.event_service.dto.CreateEventRs;
import ru.newrecon.event_service.dto.GetEventRs;
import ru.newrecon.event_service.dto.UpdateEventRq;
import ru.newrecon.event_service.dto.UpdateEventRs;
import ru.newrecon.event_service.mapper.EventMapper;
import ru.newrecon.event_service.service.EventService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/{id}")
    public GetEventRs getById(@PathVariable UUID id) {
        return eventMapper.mapToGetEventRs(
            eventService.getById(id)
        );
    }
    
    @PostMapping
    public CreateEventRs create(@RequestBody CreateEventRq request) {
        return eventMapper.mapToCreateEventRs(
            eventMapper.map(request)
        );
    }

    @PutMapping("/{id}")
    public UpdateEventRs updateById(@PathVariable UUID id, @RequestBody UpdateEventRq request) {
        return eventMapper.mapToUpdateEventRs(
            eventMapper.map(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletById(@PathVariable UUID id) {
        eventService.deleteById(id);
        
        return ResponseEntity.ok().build();
    }
    
}
