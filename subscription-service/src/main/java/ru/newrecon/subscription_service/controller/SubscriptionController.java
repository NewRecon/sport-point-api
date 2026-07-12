package ru.newrecon.subscription_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.newrecon.subscription_service.dto.CreateSubscriptionRq;
import ru.newrecon.subscription_service.dto.CreateSubscriptionRs;
import ru.newrecon.subscription_service.dto.GetSubscriptionRs;
import ru.newrecon.subscription_service.dto.SubscribeRq;
import ru.newrecon.subscription_service.dto.UnsubscribeRq;
import ru.newrecon.subscription_service.dto.UpdateSubscriptionRq;
import ru.newrecon.subscription_service.dto.UpdateSubscriptionRs;
import ru.newrecon.subscription_service.mapper.SubscriptionMapper;
import ru.newrecon.subscription_service.service.SubscriptionService;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @GetMapping("/{id}")
    public GetSubscriptionRs getById(@PathVariable UUID id) {
        return subscriptionMapper.mapToGetSubscriptionRs(
            subscriptionService.getById(id)
        );
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public CreateSubscriptionRs create(@AuthenticationPrincipal UUID userId, @RequestBody CreateSubscriptionRq request) {
        return subscriptionMapper.mapToCreateSubscriptionRs(
            subscriptionService.save(
                subscriptionMapper.map(userId, request)
            )
        );
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@AuthenticationPrincipal UUID userId, @RequestBody SubscribeRq subscribeRq) {
        subscriptionService.subscribe(userId, subscribeRq.eventId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@AuthenticationPrincipal UUID userId, @RequestBody UnsubscribeRq subscribeRq) {
        subscriptionService.unsubscribe(userId, subscribeRq.eventId());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public UpdateSubscriptionRs updateById(
        @AuthenticationPrincipal UUID userId, @PathVariable UUID id, @RequestBody UpdateSubscriptionRq request
    ) {
        return subscriptionMapper.mapToUpdateSubscriptionRs(
            subscriptionService.save(
                subscriptionMapper.map(userId, id, request)
            )
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        subscriptionService.delete(id);

        return ResponseEntity.ok().build();
    }

}
