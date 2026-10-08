package ru.newrecon.profile_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.dto.ProfileViewDto;
import ru.newrecon.profile_service.entity.File;
import ru.newrecon.profile_service.entity.Profile;
import ru.newrecon.profile_service.entity.ProfileEvent;

@Service
@RequiredArgsConstructor
public class ProfileFacade {

    private final ProfileService profileService;
    private final ProfileEventService profileEventService;
    private final FileService fileService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ProfileViewDto getById(UUID id) {
        Profile profile = profileService.getById(id);


        return createProfileViewDto(profile);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ProfileViewDto getByUserId(UUID userId) {
        Profile profile = profileService.getByUserId(userId);

        return createProfileViewDto(profile);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ProfileViewDto update(Profile profile) {
        Profile currentProfile = profileService.getByUserId(profile.getUserId());
        currentProfile.setBio(profile.getBio());
        currentProfile.setEmail(profile.getEmail());

        profileService.save(currentProfile);

        return createProfileViewDto(currentProfile);
    }

    private ProfileViewDto createProfileViewDto(Profile profile) {
        UUID userId = profile.getUserId();
        File file = fileService.findByUserId(userId);

        List<ProfileEvent> profileEvents = profileEventService.findByUserId(userId);

        List<ProfileEvent> owners = profileEvents.stream()
                .filter(ProfileEvent::isOwner)
                .toList();
        List<ProfileEvent> notOwners = profileEvents.stream()
                .filter(profileEvent -> !profileEvent.isOwner() || profileEvent.isCreatorParticipant())
                .toList();

        return buildProfileViewDto(profile, owners, notOwners, file);
    }

    private ProfileViewDto buildProfileViewDto(
            Profile profile, List<ProfileEvent> owners, List<ProfileEvent> notOwners, File file) {
        return new ProfileViewDto(
                profile.getId(),
                profile.getName(),
                profile.getUserId(),
                profile.getBio(),
                profile.getEmail(),
                file!=null ? file.getId() : null,
                owners,
                notOwners);
    }
}
