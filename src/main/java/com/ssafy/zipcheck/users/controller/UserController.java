package com.ssafy.zipcheck.users.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.users.dto.UpdateAlarmRequest;
import com.ssafy.zipcheck.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.users.dto.UpdateProfileRequest;
import com.ssafy.zipcheck.users.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /* =========================
       내 정보 조회
       ========================= */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(ApiResponse.ok(userService.getMyInfo(userId)));
    }

    /* =========================
       닉네임 수정
       ========================= */
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateProfileRequest req) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        userService.updateProfile(userId, req);

        return ResponseEntity.ok(ApiResponse.ok("프로필 수정 완료"));
    }

    /* =========================
       비밀번호 수정
       ========================= */
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<?>> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePasswordRequest req) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        userService.updatePassword(userId, req);

        return ResponseEntity.ok(ApiResponse.ok("비밀번호 수정 완료"));
    }

    /* =========================
       알림 설정
       ========================= */
    @PatchMapping("/me/alarm")
    public ResponseEntity<ApiResponse<?>> updateAlarm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateAlarmRequest req) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        userService.updateAlarm(userId, req.getAgree());

        return ResponseEntity.ok(ApiResponse.ok("알림 설정 수정 완료"));
    }

    /* =========================
       프로필 이미지 업로드 / 삭제
       ========================= */
    @PatchMapping(
            value = "/me/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<?>> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        String imageUrl = userService.updateProfileImage(userId, image);

        return ResponseEntity.ok(ApiResponse.ok(imageUrl));
    }

    /* =========================
       회원 탈퇴
       ========================= */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        userService.deleteUser(userId);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.ok("회원 탈퇴 완료"));
    }
}
