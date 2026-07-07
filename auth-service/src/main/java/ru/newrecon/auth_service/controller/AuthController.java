package ru.newrecon.auth_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.dto.LoginRq;
import ru.newrecon.auth_service.dto.LoginRs;
import ru.newrecon.auth_service.dto.RegisterRq;
import ru.newrecon.auth_service.dto.RegisterRs;
import ru.newrecon.auth_service.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/login")
    public LoginRs login (@RequestBody LoginRq request) {
        return new LoginRs(
            authService.authenticate(request.username(), request.password())
        );
    }

    @PostMapping("/register")
    public RegisterRs register (@RequestBody RegisterRq request) {
        return new RegisterRs(
            authService.register(request.username(), request.password())
        );
    }
}
