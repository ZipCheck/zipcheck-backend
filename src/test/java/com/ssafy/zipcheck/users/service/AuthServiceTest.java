package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.auth.mapper.AuthMapper;
import com.ssafy.zipcheck.auth.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Assuming BCryptPasswordEncoder is a dependency of AuthServiceImpl
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthMapper authMapper;

    @Mock
    private BCryptPasswordEncoder encoder; // Mock BCryptPasswordEncoder

    @InjectMocks
    private AuthServiceImpl authService; // Inject mocks into AuthServiceImpl

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest req = new SignupRequest();
        req.setEmail("test123@test.com");
        req.setPassword("1234");
        req.setNickname("tester");
        req.setProfileImage(null);

        // Mocking behavior
        when(authMapper.existsByEmail(anyString())).thenReturn(0);
        when(authMapper.existsByNickname(anyString())).thenReturn(0);
        when(encoder.encode(anyString())).thenReturn("encodedPassword"); // Mock encoding

        // when
        authService.signup(req);

        // then
        verify(authMapper, times(1)).existsByEmail("test123@test.com");
        verify(authMapper, times(1)).existsByNickname("tester");
        verify(encoder, times(1)).encode("1234");
        verify(authMapper, times(1)).insertUser(any(SignupRequest.class), anyString());

        // Since this is a unit test for AuthService, we typically don't verify direct database state.
        // We verify interactions with the mocked dependencies.
    }
}