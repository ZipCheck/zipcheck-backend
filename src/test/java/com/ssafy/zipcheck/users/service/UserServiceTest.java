package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.mapper.UserMapper;
import com.ssafy.zipcheck.auth.users.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

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
        int exists = userMapper.existsByEmail("test123@test.com");
        assertThat(exists).isEqualTo(1);
    }
}
