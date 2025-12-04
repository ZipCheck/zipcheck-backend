package com.ssafy.zipcheck.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(int userId, String email, boolean admin) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenValidity());
        String role = admin ? "ADMIN" : "USER";

        return Jwts.builder()
                .setSubject(email)
                .setIssuer(jwtProperties.getIssuer())
                .claim("userId", userId)
                .claim("role", role)
                .claim("admin", admin)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public int getUserId(String token) {
        Object claim = parseClaims(token).get("userId");
        if (claim instanceof Integer i) {
            return i;
        }
        if (claim instanceof Long l) {
            return l.intValue();
        }
        return Integer.parseInt(String.valueOf(claim));
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        Object claim = parseClaims(token).get("role");
        return claim != null ? claim.toString() : null;
    }

    public boolean isAdmin(String token) {
        Object claim = parseClaims(token).get("admin");
        if (claim instanceof Boolean b) {
            return b;
        }
        return Boolean.parseBoolean(String.valueOf(claim));
    }

    public Instant getExpiry(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.toInstant();
    }
}
