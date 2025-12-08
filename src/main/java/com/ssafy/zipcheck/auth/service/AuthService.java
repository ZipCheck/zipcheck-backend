package com.ssafy.zipcheck.auth.service;


import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.users.vo.User;

public interface AuthService {
    void signup(SignupRequest request);
    User login(String email, String rawPassword);
    void saveRefreshToken(String email, String token);
    String findRefreshToken(String email);
    void logout(String email);

}
