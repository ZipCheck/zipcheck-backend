package com.ssafy.zipcheck.auth.users.service;

import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
//    private final BCryptPasswordEncoder encoder;

    @Override
    public void signup(SignupRequest request) {

        if (userMapper.existsByEmail(request.getEmail()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        if (userMapper.existsByNickname(request.getNickname()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        request.setPassword(request.getPassword());

        userMapper.insertUser(request);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        request.setNewPassword(request.getNewPassword());

        int updated = userMapper.updatePassword(request);

        if (updated == 0)
            throw new IllegalArgumentException("비밀번호 변경 실패");
    }
}
