package com.ssafy.zipcheck.auth.users.service;


import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;

public interface UserService {
    void signup(SignupRequest request);
    void updatePassword(UpdatePasswordRequest request);
}
