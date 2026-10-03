package ru.newrecon.subscription_service.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.newrecon.subscription_service.dto.auth.PrincipalDto;

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        
        String userId = request.getHeader("X-UserId");
        String rolesHeader = request.getHeader("X-Roles");
        String username = request.getHeader("X-Username");

        if (Strings.isEmpty(userId)) {
            filterChain.doFilter(request, response);
            return;
        }

        List<SimpleGrantedAuthority> roles = List.of();

        if (Strings.isNotEmpty(rolesHeader)) {
            roles = Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }

        PrincipalDto principalDto = new PrincipalDto(UUID.fromString(userId), username);

        UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(principalDto, null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

}
