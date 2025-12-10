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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<?> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            if (userDetails == null) {
                return ApiResponse.badRequest("인증 정보가 없습니다.");
            }
            int userId = userDetails.getUser().getUserId();
            return ApiResponse.ok(userService.getMyInfo(userId));
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            log.info("Exception: {}", e.getMessage());
            return ApiResponse.internalError("내 정보 조회 중 오류가 발생했습니다.");
        }
    }

    @PatchMapping("/me")
    public ApiResponse<?> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateProfileRequest req) {

        try {
            if (userDetails == null) {
                return ApiResponse.badRequest("인증 정보가 없습니다.");
            }
            int userId = userDetails.getUser().getUserId();
            userService.updateProfile(userId, req);
            return ApiResponse.ok("프로필 수정 완료");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalError("프로필 수정 중 오류가 발생했습니다.");
        }
    }

    @PatchMapping("/me/password")
    public ApiResponse<?> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePasswordRequest req) {

        try {
            if (userDetails == null) {
                return ApiResponse.badRequest("인증 정보가 없습니다.");
            }
            int userId = userDetails.getUser().getUserId();
            userService.updatePassword(userId, req);
            return ApiResponse.ok("비밀번호 수정 완료");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalError("비밀번호 수정 중 오류가 발생했습니다.");
        }
    }

    @PatchMapping("/me/alarm")
    public ApiResponse<?> updateAlarm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateAlarmRequest req) {

        try {
            if (userDetails == null) {
                return ApiResponse.badRequest("인증 정보가 없습니다.");
            }
            int userId = userDetails.getUser().getUserId();
            userService.updateAlarm(userId, req.getAgree());
            return ApiResponse.ok("알림 설정 수정 완료");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalError("알림 설정 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/me")
    public ApiResponse<?> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) {

        try {
            if (userDetails == null) {
                return ApiResponse.badRequest("인증 정보가 없습니다.");
            }
            int userId = userDetails.getUser().getUserId();
            userService.deleteUser(userId);
            Cookie cookie = new Cookie("refresh", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ApiResponse.ok("회원 탈퇴 완료");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalError("회원 탈퇴 중 오류가 발생했습니다.");
        }
    }
}
