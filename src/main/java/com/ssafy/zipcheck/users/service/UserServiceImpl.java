package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.users.mapper.UserMapper;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(String email, String rawPassword) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .nickname(null)
                .profile_image(null)
                .status(true)
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userMapper.save(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int userId) {
        return userMapper.findById(userId);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userMapper.deleteById(userId);
    }
}
