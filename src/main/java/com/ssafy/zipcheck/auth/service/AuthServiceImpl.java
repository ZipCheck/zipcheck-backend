package com.ssafy.zipcheck.auth.service;

import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.auth.mapper.AuthMapper;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void signup(SignupRequest request) {

        if (authMapper.existsByEmail(request.getEmail()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        if (authMapper.existsByNickname(request.getNickname()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        request.setPassword(encoder.encode(request.getPassword()));

        // 기본 역할 설정
        String role = "ROLE_USER";

        authMapper.insertUser(request, role);
    }

    @Override
    public User login(String email, String rawPassword) {
        User user = authMapper.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        if (user.getStatus() == 0) {
            throw new IllegalArgumentException("비활성화된 계정입니다.");
        }

        return user;
    }

    @Override
    public void saveRefreshToken(String email, String token) {
        authMapper.saveRefreshToken(email, token);
    }

    @Override
    public String findRefreshToken(String email) {
        return authMapper.findRefreshToken(email);
    }

    @Override
    public void logout(String email) {
        authMapper.deleteRefreshToken(email);
    }

}
