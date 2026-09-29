package ru.newrecon.event_service.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.event_service.dto.GetEventRs;
import ru.newrecon.event_service.dto.Participant;
import ru.newrecon.event_service.entity.Event;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventAggregatorService {

    private final EventService eventService;
    private final RestClient subscriptionRestClient;
    private final RestClient profileRestClient;
 
    public GetEventRs getEventPageData(UUID eventId) {

        Event event = eventService.getById(eventId);

        List<UUID> userIds = List.of();
        try {
            userIds = subscriptionRestClient.get()
                    .uri("/event/{eventId}/user-ids", eventId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UUID>>() {});
        } catch (Exception e) {
            log.error("Ошибка при получении подписки", e);
        }

        List<Participant> participants = List.of();
        // if (userIds != null && !userIds.isEmpty()) {
        //     String idsParam = userIds.stream()
        //             .map(String::valueOf)
        //             .collect(Collectors.joining(","));

        //     try {
        //         participants = profileRestClient.get()
        //                 .uri(uriBuilder -> uriBuilder.queryParam("ids", idsParam).build())
        //                 .retrieve()
        //                 .body(new ParameterizedTypeReference<List<Participant>>() {});
        //     } catch (Exception e) {
        //         log.error("Ошибка при получении профилей пользователей", e);
        //     }
        // }

        return buildGetEventRs(event, participants);
    }

    private GetEventRs buildGetEventRs(Event event, List<Participant> participants) {
        return new GetEventRs();
    }
}
