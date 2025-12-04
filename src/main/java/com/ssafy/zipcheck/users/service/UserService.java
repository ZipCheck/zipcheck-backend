package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.users.vo.User;

import java.util.Optional;

public interface UserService {
    User createUser(String email, String rawPassword);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int userId);
    void deleteUser(int userId);
}
