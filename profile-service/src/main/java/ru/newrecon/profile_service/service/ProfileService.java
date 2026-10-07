package ru.newrecon.profile_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.entity.Profile;
import ru.newrecon.profile_service.kafka.payload.CreateUserPayload;
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

    // TODO воняет
    public void create(CreateUserPayload createUserDto) {
        Profile profile = new Profile();
        profile.setUserId(createUserDto.userId());
        profile.setName(createUserDto.name());
        profile.setName(createUserDto.name());
        profile.setEmail(createUserDto.email());
        
        save(profile);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Profile update(Profile profile) {
        Profile currentProfile = getByUserId(profile.getUserId());
        currentProfile.setBio(profile.getBio());
        currentProfile.setEmail(profile.getEmail());

        return profileRepository.save(currentProfile);
    }

    public void delete(UUID id) {
        profileRepository.deleteById(id);
    }
}
