package com.ssafy.zipcheck.users.service;

import com.ssafy.zipcheck.auth.mapper.AuthMapper;
import com.ssafy.zipcheck.global.service.S3UploaderService;
import com.ssafy.zipcheck.users.dto.MyInfoResponse;
import com.ssafy.zipcheck.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.users.dto.UpdateProfileRequest;
import com.ssafy.zipcheck.users.mapper.UserMapper;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final S3UploaderService s3UploaderService;

    public MyInfoResponse getMyInfo(int userId) {
        MyInfoResponse info = userMapper.findMyInfo(userId);
        if (info == null) {
            throw new IllegalArgumentException("사용자 없음");
        }
        return info;
    }

    /* =========================
       닉네임 수정 (안전)
       ========================= */
    public void updateProfile(int userId, UpdateProfileRequest req) {
        if (req == null) return;

        String nickname = req.getNickname();
        if (nickname == null || nickname.isBlank()) {
            return; // 닉네임 수정 의도 없음
        }

        userMapper.updateNickname(userId, nickname);
    }

    /* =========================
       프로필 이미지 업로드 / 삭제
       ========================= */
    public String updateProfileImage(int userId, MultipartFile image) {

        // 사진 삭제
        if (image == null || image.isEmpty()) {
            userMapper.updateProfileImage(userId, null);
            return null;
        }

        // 사진 업로드
        String imageUrl = s3UploaderService.uploadProfileImage(image, userId);
        userMapper.updateProfileImage(userId, imageUrl);
        return imageUrl;
    }

    /* =========================
       비밀번호 수정
       ========================= */
    public void updatePassword(int userId, UpdatePasswordRequest req) {
        String current = userMapper.getPassword(userId);
        if (!passwordEncoder.matches(req.getCurrentPassword(), current)) {
            throw new IllegalArgumentException("현재 비밀번호 불일치");
        }

        userMapper.updatePassword(
                userId,
                passwordEncoder.encode(req.getNewPassword())
        );
    }

    /* =========================
       알림 설정
       ========================= */
    public void updateAlarm(int userId, Boolean agree) {
        if (userMapper.updateAlarmSetting(userId, agree) == 0) {
            userMapper.insertAlarmSetting(userId, agree);
        }
    }

    /* =========================
       회원 탈퇴
       ========================= */
    public void deleteUser(int userId) {
        User user = userMapper.findById(userId);
        userMapper.deleteUser(userId);
        authMapper.deleteRefreshToken(user.getEmail());
    }
}
