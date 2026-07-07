package ru.newrecon.auth_service.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.exception.UnauthorizedException;
import ru.newrecon.auth_service.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {

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

    public String register(String username, String password) {

        User newUser = new User();
        newUser.setName(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(Set.of());

        User user = (User) userService.save(newUser);

        return jwtProvider.generateToken(user); 
    }
}
