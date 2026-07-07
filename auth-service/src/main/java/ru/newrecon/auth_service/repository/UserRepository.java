package ru.newrecon.auth_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.newrecon.auth_service.entity.User;


public interface UserRepository extends JpaRepository<User, UUID> {

    User findByName(String name);
}
