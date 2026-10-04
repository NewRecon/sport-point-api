package ru.newrecon.subscription_service.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.subscription_service.service.outbox.OutboxMessageDispatcher;
import ru.newrecon.subscription_service.service.outbox.OutboxMessageService;

@Slf4j 
@Component 
@RequiredArgsConstructor 
public class OutboxScheduler {

    private final OutboxMessageDispatcher outboxFacade;
    private final OutboxMessageService outboxMessageService;

    @Scheduled(fixedDelayString = "${outbox.scheduler.send-delay-ms}")
    public void sendEvents() {
        log.info("запущена отправка sendEvents");
        outboxFacade.runSendProcess();
    }

    @Scheduled(cron = "${outbox.scheduler.delete-crone}")
    public void deleteEventsinSentStatus() {
        log.info("запущена удаление sendEvents в статусе SENT");
        outboxMessageService.deleteAllInSentStatus();
    }
}
