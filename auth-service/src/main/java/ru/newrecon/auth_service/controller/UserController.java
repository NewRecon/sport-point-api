package ru.newrecon.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.dto.AssignRoleRq;
import ru.newrecon.auth_service.dto.RevokeRoleRq;
import ru.newrecon.auth_service.service.UserService;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignRole")
    public ResponseEntity<Void> assignRole(@RequestBody AssignRoleRq request) {
        userService.assignRole(request.userId(), request.role());
        
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/revokeRole")
    public ResponseEntity<Void> revokeRole(@RequestBody RevokeRoleRq request) {
        userService.revokeRole(request.userId(), request.role());
        
        return ResponseEntity.ok().build();
    }
}
