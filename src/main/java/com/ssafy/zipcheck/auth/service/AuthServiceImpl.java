package com.ssafy.zipcheck.auth.service;

import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.jwt.JwtProperties;
import com.ssafy.zipcheck.jwt.JwtTokenProvider;
import com.ssafy.zipcheck.security.TokenBlacklist;
import com.ssafy.zipcheck.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklist tokenBlacklist;
    private final JwtProperties jwtProperties;


    @Override
    @Transactional
    public void signup(SignupRequest request) {
        userService.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already registered");
        });
        userService.createUser(request.getEmail(), request.getPassword());
    }

    @Override
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        String prefix = jwtProperties.getPrefix();
        if (!token.startsWith(prefix)) {
            return;
        }
        String rawToken = token.substring(prefix.length());
        if (jwtTokenProvider.validateToken(rawToken)) {
            tokenBlacklist.blacklist(rawToken, jwtTokenProvider.getExpiry(rawToken));
        }
    }
}
