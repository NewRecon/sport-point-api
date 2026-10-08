package ru.newrecon.profile_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.dto.CreateProfileRq;
import ru.newrecon.profile_service.dto.CreateProfileRs;
import ru.newrecon.profile_service.dto.GetProfileViewRs;
import ru.newrecon.profile_service.dto.UpdateProfileRq;
import ru.newrecon.profile_service.mapper.ProfileMapper;
import ru.newrecon.profile_service.service.ProfileFacade;
import ru.newrecon.profile_service.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileFacade profileFacade;
    private final ProfileMapper profileMapper;

    @GetMapping
    public GetProfileViewRs get(@AuthenticationPrincipal UUID id) {
        return profileMapper.mapToGetProfileRs(
            profileFacade.getByUserId(id)
        );
    }
    
    @GetMapping("/{id}")
    public GetProfileViewRs getById(@PathVariable UUID id) {
        return profileMapper.mapToGetProfileRs(
            profileFacade.getById(id)
        );
    }

    @GetMapping("/user/{userId}")
    public GetProfileViewRs getByUserId(@PathVariable UUID userId) {
        return profileMapper.mapToGetProfileRs(
            profileFacade.getByUserId(userId)
        );
    }
    
    @PostMapping
    public CreateProfileRs create(@AuthenticationPrincipal UUID userId, @RequestBody CreateProfileRq request) {
        return profileMapper.mapToCreateProfileRs(
            profileService.save(
                profileMapper.map(userId, request)
            )
        );
    }

    @PutMapping
    public GetProfileViewRs update(@AuthenticationPrincipal UUID userId, @RequestBody UpdateProfileRq request) {
        return profileMapper.mapToUpdateProfileRs(
            profileFacade.update(
                profileMapper.map(userId, request)
            )
        );
    }

    @PutMapping("/{id}")
    public GetProfileViewRs updateById(
        @AuthenticationPrincipal UUID userId, @PathVariable UUID id, @RequestBody UpdateProfileRq request
    ) {
        return profileMapper.mapToUpdateProfileRs(
            profileFacade.update(
                profileMapper.map(userId, id, request)
            )
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        profileService.delete(id);

        return ResponseEntity.ok().build();
    }
    
}
