package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.users.mapper.UserMapper;
import com.ssafy.zipcheck.users.vo.User;
import org.junit.jupiter.api.BeforeEach; // 추가
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    // @InjectMocks 제거
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;

    // @BeforeEach: 각각의 테스트가 실행되기 전에 이 메소드를 먼저 실행
    @BeforeEach
    void setUp() {
        // 실제 PasswordEncoder와 Mock UserMapper를 사용하여
        // UserServiceImpl를 수동으로 생성 및 주입합니다.
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userMapper, passwordEncoder);
    }


    @Test
    @DisplayName("회원가입 시 비밀번호가 올바르게 암호화되고, 암호화된 비밀번호는 원본과 matches 되어야 한다")
    void passwordEncodingAndMatching_ShouldWorkCorrectly() {
        // given (준비)
        String userEmail = "test@example.com";
        String rawPassword = "1234";

        // when (실행)
        User createdUser = userService.createUser(userEmail, rawPassword);
        String encodedPassword = createdUser.getPassword();

        // then (검증)
        System.out.println("원본 비밀번호: " + rawPassword);
        System.out.println("암호화된 비밀번호: " + encodedPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        verify(userMapper).save(any(User.class));
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrong_password", encodedPassword));
    }
}