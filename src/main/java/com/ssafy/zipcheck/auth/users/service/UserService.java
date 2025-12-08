package com.ssafy.zipcheck.auth.users.service;


import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.vo.User;

public interface UserService {
    void signup(SignupRequest request);
    void updatePassword(UpdatePasswordRequest request);
    User login(String email, String rawPassword);
    void saveRefreshToken(String email, String token);

}
