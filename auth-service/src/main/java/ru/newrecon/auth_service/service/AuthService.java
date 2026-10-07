package ru.newrecon.auth_service.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.exception.UnauthorizedException;
import ru.newrecon.auth_service.security.JwtProvider;
import ru.newrecon.auth_service.service.user.UserOutboxFacade;
import ru.newrecon.auth_service.service.user.UserService;

@Slf4j 
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserOutboxFacade userOutboxFacade;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {

        log.info("username - " + username);
        log.info("password - " + password);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Неверный логин или пароль");
        }

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("Непредвиденная ошибка при аутентификации пользователя");
        }

        User user = (User) authentication.getPrincipal();

        return jwtProvider.generateToken(user);
    }

    @Transactional 
    public String register(String username, String password, String email, String name) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        newUser.setRoles(Set.of());

        User user = (User) userService.save(newUser);

        userOutboxFacade.saveCreateUserEvent(user, email);

        return jwtProvider.generateToken(user); 
    }
}
