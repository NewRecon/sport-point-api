package ru.newrecon.profile_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.profile_service.entity.SubscriptionHistory;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, UUID> {

}
