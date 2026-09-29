package ru.newrecon.event_service.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.event_service.service.EventService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredEventScheduler {

    private final EventService eventService;

    @Scheduled(cron = "${scheduler.delete-event.crone}")
    void deleteExpiredEventScheduler() {
        int count = eventService.deleteExpired();
        log.info(count + " просроченных ивентов было удалено");
    }
}
