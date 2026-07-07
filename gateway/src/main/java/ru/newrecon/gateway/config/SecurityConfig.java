package ru.newrecon.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import lombok.RequiredArgsConstructor;
import ru.newrecon.gateway.security.JwtAuthenticationFilter;
import ru.newrecon.gateway.security.JwtProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity  http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(new JwtAuthenticationFilter(jwtProvider), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint((exchange, e) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                    })
                )
                .authorizeExchange(auth -> auth
                    .anyExchange().permitAll()
                )
                .build();
    }
}
