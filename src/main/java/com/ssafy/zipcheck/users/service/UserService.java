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
        if (info == null) throw new IllegalArgumentException("사용자 없음");
        return info;
    }

    public void updateProfile(int userId, UpdateProfileRequest req) {
        userMapper.updateProfile(userId, req.getNickname(), req.getProfileImageUrl());
    }

    public String updateProfileImage(int userId, MultipartFile image) {
        String imageUrl = s3UploaderService.uploadProfileImage(image, userId);
        userMapper.updateProfile(userId, null, imageUrl);
        return imageUrl;
    }

    public void updatePassword(int userId, UpdatePasswordRequest req) {
        String current = userMapper.getPassword(userId);
        if (!passwordEncoder.matches(req.getCurrentPassword(), current)) {
            throw new IllegalArgumentException("현재 비밀번호 불일치");
        }
        userMapper.updatePassword(userId, passwordEncoder.encode(req.getNewPassword()));
    }

    public void updateAlarm(int userId, Boolean agree) {
        if (userMapper.updateAlarmSetting(userId, agree) == 0) {
            userMapper.insertAlarmSetting(userId, agree);
        }
    }

    public void deleteUser(int userId) {
        User user = userMapper.findById(userId);
        userMapper.deleteUser(userId);
        authMapper.deleteRefreshToken(user.getEmail());
    }
}
