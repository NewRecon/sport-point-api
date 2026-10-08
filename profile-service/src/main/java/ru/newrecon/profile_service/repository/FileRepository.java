package ru.newrecon.profile_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.profile_service.entity.File;

public interface FileRepository extends JpaRepository<File, UUID> {

    Optional<File> findByUserId(UUID userId);
}
