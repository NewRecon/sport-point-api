package ru.newrecon.auth_service.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.auth_service.service.outbox.OutboxDispatcher;

@Slf4j 
@Component 
@RequiredArgsConstructor 
public class OutboxScheduler {

    private final OutboxDispatcher outboxFacade;

    @Scheduled(cron = "${scheduler.outbox.crone}")
    public void sendEvents() {
        outboxFacade.sendAll();
    }
}
