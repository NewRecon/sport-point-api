package ru.newrecon.auth_service.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ru.newrecon.auth_service.entity.User;

@Component
public class JwtProvider {

    @Value("${jwt.key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Date exp = Date.from(Instant.now().plusMillis(expirationTime));

        return Jwts.builder()
                .signWith(key)
                .claim(ChillClaim.USER_ID.name(), user.getId())
                .claim(ChillClaim.ROLES.name(), authorities)
                .expiration(exp)
                .compact();
    }

    public static enum ChillClaim {
        USER_ID,
        ROLES
    }
}
