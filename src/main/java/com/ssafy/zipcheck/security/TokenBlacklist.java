package com.ssafy.zipcheck.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private final Map<String, Instant> blacklisted = new ConcurrentHashMap<>();

    public void blacklist(String token, Instant expiry) {
        blacklisted.put(token, expiry);
    }

    public boolean isBlacklisted(String token) {
        Instant expiry = blacklisted.get(token);
        if (expiry == null) {
            return false;
        }
        if (Instant.now().isAfter(expiry)) {
            blacklisted.remove(token);
            return false;
        }
        return true;
    }
}
