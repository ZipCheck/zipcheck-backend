package com.ssafy.zipcheck.auth.service;

import com.ssafy.zipcheck.auth.dto.SignupRequest;

public interface AuthService {
    void signup(SignupRequest request);
    void logout(String token);
}
