package ru.newrecon.auth_service.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.entity.enums.Role;
import ru.newrecon.auth_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    public UserDetails getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найден ползователь с id : " + id));
    }

    public UserDetails save(User user) {
        return userRepository.save(user);
    }

    public void assignRole(UUID userId, Role role) {
        User user = (User) getById(userId);
        user.getAuthorities().add(role);

        userRepository.save(user);
    }

    public void revokeRole(UUID userId, Role role) {
        User user = (User) getById(userId);
        user.getAuthorities().remove(role);

        userRepository.save(user);
    }
}
