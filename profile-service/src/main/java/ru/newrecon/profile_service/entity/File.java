package ru.newrecon.profile_service.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
public class File {
    @Id
    private UUID id;
    private UUID userId;
}
