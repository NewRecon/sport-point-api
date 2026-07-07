package ru.newrecon.profile_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.profile_service.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID>{

    boolean existByName(String name);
}
