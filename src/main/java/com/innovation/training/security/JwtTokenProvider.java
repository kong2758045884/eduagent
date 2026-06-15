package com.innovation.training.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(String username) {
        return generateToken(username, null);
    }

    public String generateToken(String username, String teacherType) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expiration);
        var builder = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiresAt);
        if (teacherType != null && !teacherType.isBlank()) {
            builder.claim("teacherType", teacherType);
        }
        return builder.signWith(secretKey).compact();
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String getTeacherType(String token) {
        return parseClaims(token).get("teacherType", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
