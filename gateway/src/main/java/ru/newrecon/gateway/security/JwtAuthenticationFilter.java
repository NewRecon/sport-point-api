package ru.newrecon.gateway.security;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ru.newrecon.gateway.enums.Role;
import ru.newrecon.gateway.security.JwtProvider.ChillClaim;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtProvider jwtProvider;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
            return chain.filter(exchange);
        }

        try {
            String token = authHeader.substring(7);

            UUID userId = jwtProvider.findClaim(token, ChillClaim.USER_ID);
            Set<Role> roles = jwtProvider.findClaim(token, ChillClaim.ROLES);

            String rolesHeader = roles.isEmpty() ? null : roles.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(","));

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-UserId", userId.toString())
                    .header("X-Roles", rolesHeader)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (ExpiredJwtException e) {
            log.warn("Токен просрочен: {}", e.getMessage());
            return setResponse(exchange, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (MalformedJwtException | SignatureException e) {
            log.warn("Невалидный токен: {}", e.getMessage());
            return setResponse(exchange, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обработке токена", e);
            return setResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    private Mono<Void> setResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            ObjectNode objectNode = objectMapper.createObjectNode().put("message", message);
            byte[] bytes = objectMapper.writeValueAsBytes(objectNode);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Ошибка сериализации JSON ответа", e);
            return response.setComplete();
        }
    }
}
