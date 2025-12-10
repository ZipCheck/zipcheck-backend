package com.ssafy.zipcheck.auth.service;

import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.auth.mapper.AuthMapper;
import com.ssafy.zipcheck.auth.util.EmailAuthStorage;
import com.ssafy.zipcheck.auth.util.MailService;
import com.ssafy.zipcheck.users.mapper.UserMapper;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;
    private final EmailAuthStorage emailAuthStorage;
    private final MailService mailService;

    @Override
    public void signup(SignupRequest request) {

        if (authMapper.existsByEmail(request.getEmail()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        if (authMapper.existsByNickname(request.getNickname()) > 0)
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        request.setPassword(encoder.encode(request.getPassword()));
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

    @Override
    public void sendResetPasswordMail(String email) {

        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("해당 이메일로 가입한 유저가 없습니다.");
        }

        // 새 요청이면 기존 코드 무효화
        emailAuthStorage.delete(email);

        // 더 안전한 6자리 숫자 코드를 생성
        String code = String.format("%06d", (int)(Math.random() * 1000000));

        emailAuthStorage.save(email, code);

        mailService.sendMail(email, "비밀번호 재설정 코드",
                "인증코드: " + code + "\n(5분간 유효합니다)");
    }

    @Override
    public void resetPassword(String email, String code) {

        String saved = emailAuthStorage.get(email);

        if (saved == null) {
            throw new IllegalArgumentException("인증코드가 만료되었거나 존재하지 않습니다.");
        }

        if (!saved.equals(code)) {
            throw new IllegalArgumentException("인증코드가 올바르지 않습니다.");
        }

        // 임시 비밀번호 생성
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String encoded = encoder.encode(tempPassword);

        userMapper.updatePasswordByEmail(email, encoded);

        // 임시 비밀번호 메일 전송
        mailService.sendMail(email,
                "임시 비밀번호",
                "임시 비밀번호: " + tempPassword + "\n로그인 후 반드시 비밀번호를 변경하세요."
        );

        // 인증코드 폐기
        emailAuthStorage.delete(email);
    }

}
