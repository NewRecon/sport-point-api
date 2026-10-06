package ru.newrecon.event_service.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.dto.CreateEventRq;
import ru.newrecon.event_service.dto.CreateEventRs;
import ru.newrecon.event_service.dto.DeleteEventRq;
import ru.newrecon.event_service.dto.GetEventRs;
import ru.newrecon.event_service.dto.GetViewEventRs;
import ru.newrecon.event_service.dto.UpdateEventRq;
import ru.newrecon.event_service.dto.UpdateEventRs;
import ru.newrecon.event_service.dto.auth.PrincipalDto;
import ru.newrecon.event_service.enums.EventCategory;
import ru.newrecon.event_service.mapper.EventMapper;
import ru.newrecon.event_service.service.EventAggregatorService;
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
    private final EventAggregatorService eventAggregatorService;

    @GetMapping("/{id}")
    public GetViewEventRs getViewById(@PathVariable UUID id) {
        return eventMapper.mapToGetViewEventRs(
                eventAggregatorService.getEventViewData(id));
    }

    @GetMapping
    public List<GetEventRs> findAllActive(
            @RequestParam(required = false) EventCategory category,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) boolean onlyAvailable) {
        return eventMapper.mapToGetEventRs(
                eventService.findAllActiveWithFilters(category, dateFrom, dateTo, onlyAvailable));
    }

    @PostMapping
    public CreateEventRs create(@AuthenticationPrincipal PrincipalDto principal, @RequestBody CreateEventRq request) {
        return eventMapper.mapToCreateEventRs(
                eventAggregatorService.createEvent(
                        eventMapper.map(principal.userId(), principal.username(), request),
                        principal.username(),
                        request.isCreatorParticipant()
                    )
                );
    }

    @PutMapping("/{id}")
    public UpdateEventRs updateById(
            @AuthenticationPrincipal PrincipalDto principal, @PathVariable UUID id,
            @RequestBody UpdateEventRq request) {
        return eventMapper.mapToUpdateEventRs(
                eventService.save(eventMapper.map(principal.userId(), id, request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        eventService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteEventRq request) {
        eventService.delete(request.eventId());

        return ResponseEntity.ok().build();
    }
}
