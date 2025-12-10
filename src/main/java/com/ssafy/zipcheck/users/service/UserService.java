package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.auth.mapper.AuthMapper;
import com.ssafy.zipcheck.users.dto.MyInfoResponse;
import com.ssafy.zipcheck.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.users.dto.UpdateProfileRequest;
import com.ssafy.zipcheck.users.mapper.UserMapper;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public MyInfoResponse getMyInfo(int userId) {
        MyInfoResponse info = userMapper.findMyInfo(userId);
        if (info == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }
        return info;
    }

    public void updateProfile(int userId, UpdateProfileRequest req) {

        // 닉네임 검증 (필요 시 정책에 맞게 조정)
        if (req.getNickname() == null || req.getNickname().isBlank()) {
            throw new IllegalArgumentException("닉네임을 입력하세요.");
        }

        int updated = userMapper.updateProfile(
                userId,
                req.getNickname(),
                req.getProfileImage()
        );

        if (updated == 0) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    public void updatePassword(int userId, UpdatePasswordRequest req) {

        String currentPw = userMapper.getPassword(userId);

        if (currentPw == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(req.getCurrentPassword(), currentPw)) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호 검증
        if (req.getNewPassword() == null || req.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("새 비밀번호를 입력하세요.");
        }

        // 새 비밀번호가 기존 비밀번호와 동일한지 체크
        if (passwordEncoder.matches(req.getNewPassword(), currentPw)) {
            throw new IllegalArgumentException("새 비밀번호는 기존 비밀번호와 달라야 합니다.");
        }

        String encoded = passwordEncoder.encode(req.getNewPassword());
        int updated = userMapper.updatePassword(userId, encoded);

        if (updated == 0) {
            throw new IllegalArgumentException("비밀번호 변경에 실패했습니다.");
        }
    }

    public void updateAlarm(int userId, Boolean agree) {

        if (agree == null) {
            throw new IllegalArgumentException("알림 설정 값이 필요합니다.");
        }

        int updated = userMapper.updateAlarmSetting(userId, agree);

        // 알림 설정이 아직 없으면 새로 INSERT
        if (updated == 0) {
            userMapper.insertAlarmSetting(userId, agree);
        }
    }

    public void deleteUser(int userId) {

        // 1) 유저 정보 조회 (email 필요)
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 2) users.status = 0 (회원 탈퇴)
        int updated = userMapper.deleteUser(userId);
        if (updated == 0) {
            throw new IllegalArgumentException("회원 탈퇴에 실패했습니다.");
        }

        // 3) refresh token 삭제
        authMapper.deleteRefreshToken(user.getEmail());
    }

}
