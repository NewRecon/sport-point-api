package ru.newrecon.subscription_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.subscription_service.entity.InboxMessage;

public interface InboxMessageRepository extends JpaRepository<InboxMessage, UUID> {

}
