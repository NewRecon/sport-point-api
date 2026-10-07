package ru.newrecon.profile_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.profile_service.entity.ProfileEvent;

public interface ProfileEventRepository extends JpaRepository<ProfileEvent, UUID> {

    List<ProfileEvent> findByUserId(UUID userId);
}
