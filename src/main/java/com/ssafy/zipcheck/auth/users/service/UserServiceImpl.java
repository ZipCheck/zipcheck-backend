package com.ssafy.zipcheck.auth.users.service;

import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.mapper.UserMapper;
import com.ssafy.zipcheck.auth.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void signup(SignupRequest request) {

        if (userMapper.existsByEmail(request.getEmail()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        if (userMapper.existsByNickname(request.getNickname()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        request.setPassword(encoder.encode(request.getPassword()));

        // 기본 역할 설정
        String role = "ROLE_USER";

        userMapper.insertUser(request, role);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        request.setNewPassword(encoder.encode(request.getNewPassword()));

        int updated = userMapper.updatePassword(request);

        if (updated == 0)
            throw new IllegalArgumentException("비밀번호 변경 실패");
    }

    @Override
    public User login(String email, String rawPassword) {
        User user = userMapper.findByEmail(email)
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
        userMapper.saveRefreshToken(email, token);
    }

}
