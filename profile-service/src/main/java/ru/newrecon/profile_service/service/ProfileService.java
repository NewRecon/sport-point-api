package ru.newrecon.profile_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.dto.kafka.CreateUserDto;
import ru.newrecon.profile_service.entity.Profile;
import ru.newrecon.profile_service.repository.ProfileRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Profile getById(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найден профиль с id : " + id));
    }

    public Profile getByUserId(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Не найден профиль с userId : " + userId));
    }


    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public void create(CreateUserDto createUserDto) {
        Profile profile = new Profile();
        profile.setUserId(createUserDto.userId());
        profile.setName(createUserDto.name());

        save(profile);
    }

    public void delete(UUID id) {
        profileRepository.deleteById(id);
    }
}
