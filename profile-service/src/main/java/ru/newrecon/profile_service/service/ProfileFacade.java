package ru.newrecon.profile_service.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.dto.ProfileViewDto;
import ru.newrecon.profile_service.entity.Profile;
import ru.newrecon.profile_service.entity.ProfileEvent;

@Service
@RequiredArgsConstructor
public class ProfileFacade {

    private final ProfileService profileService;
    private final ProfileEventService profileEventService;

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

    private ProfileViewDto createProfileViewDto(Profile profile) {
        List<ProfileEvent> profileEvents = profileEventService.findByUserId(profile.getUserId());

        List<ProfileEvent> owners = profileEvents.stream()
                .filter(ProfileEvent::isOwner)
                .toList();
        List<ProfileEvent> notOwners = profileEvents.stream()
                .filter(profileEvent -> !profileEvent.isOwner() || profileEvent.isCreatorParticipant())
                .toList();

        return buildProfileViewDto(profile, owners, notOwners);
    }

    private ProfileViewDto buildProfileViewDto(
            Profile profile, List<ProfileEvent> owners, List<ProfileEvent> notOwners) {
        return new ProfileViewDto(
                profile.getId(),
                profile.getName(),
                profile.getUserId(),
                profile.getBio(),
                profile.getEmail(),
                owners,
                notOwners);
    }
}
