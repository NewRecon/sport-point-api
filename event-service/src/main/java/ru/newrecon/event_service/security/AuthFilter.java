package ru.newrecon.event_service.security;

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

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        
        String userId = request.getHeader("X-UserId");
        String rolesHeader = request.getHeader("X-Roles");

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

        UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(UUID.fromString(userId), null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

}
