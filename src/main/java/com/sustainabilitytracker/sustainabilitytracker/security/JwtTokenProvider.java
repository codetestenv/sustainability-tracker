package com.sustainabilitytracker.sustainabilitytracker.security;


import com.sustainabilitytracker.sustainabilitytracker.config.JwtProperties;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private String generateAccessToken(User user){
        return generateToken(user,jwtProperties.getAccessTokenExpiration());
    }

    private String generateRefreshToken(User user){
        return generateToken(user,jwtProperties.getRefreshTokenExpiration());
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getFullName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .compact();
    }

    public boolean isTokenValid(String token){
        try {
            var claims = getClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmailFromToken(String token){
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtProperties.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}