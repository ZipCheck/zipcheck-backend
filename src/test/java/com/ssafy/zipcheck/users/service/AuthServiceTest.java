package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.auth.mapper.AuthMapper;
import com.ssafy.zipcheck.auth.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthServiceImpl userService;

    @Autowired
    private AuthMapper authMapper;

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest req = new SignupRequest();
        req.setEmail("test123@test.com");
        req.setPassword("1234");
        req.setNickname("tester");
        req.setProfileImage(null);

        // when
        userService.signup(req);

        // then
        int exists = authMapper.existsByEmail("test123@test.com");
        assertThat(exists).isEqualTo(1);
    }
}
