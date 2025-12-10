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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            if (userDetails == null) {
                log.warn("[GET /users/me] 인증 정보 없음");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            return ResponseEntity.ok(ApiResponse.ok(userService.getMyInfo(userId)));

        } catch (IllegalArgumentException e) {
            log.warn("[GET /users/me] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("요청 값을 다시 확인해주세요."));
        } catch (Exception e) {
            log.error("[GET /users/me] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("내 정보 조회 중 문제가 발생했습니다."));
        }
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateProfileRequest req) {

        try {
            if (userDetails == null) {
                log.warn("[PATCH /users/me] 인증 정보 없음");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            userService.updateProfile(userId, req);

            return ResponseEntity.ok(ApiResponse.ok("프로필 수정 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[PATCH /users/me] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("프로필 수정 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[PATCH /users/me] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("프로필 수정 중 문제가 발생했습니다."));
        }
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<?>> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePasswordRequest req) {

        try {
            if (userDetails == null) {
                log.warn("[PATCH /users/me/password] 인증 정보 없음");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            userService.updatePassword(userId, req);

            return ResponseEntity.ok(ApiResponse.ok("비밀번호 수정 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[PATCH /users/me/password] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("비밀번호 변경 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[PATCH /users/me/password] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("비밀번호 수정 중 문제가 발생했습니다."));
        }
    }

    @PatchMapping("/me/alarm")
    public ResponseEntity<ApiResponse<?>> updateAlarm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateAlarmRequest req) {

        try {
            if (userDetails == null) {
                log.warn("[PATCH /users/me/alarm] 인증 정보 없음");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            userService.updateAlarm(userId, req.getAgree());

            return ResponseEntity.ok(ApiResponse.ok("알림 설정 수정 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[PATCH /users/me/alarm] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("알림 설정 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[PATCH /users/me/alarm] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("알림 설정 수정 중 문제가 발생했습니다."));
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response) {

        try {
            if (userDetails == null) {
                log.warn("[DELETE /users/me] 인증 정보 없음");
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

        } catch (IllegalArgumentException e) {
            log.warn("[DELETE /users/me] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("회원 탈퇴 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[DELETE /users/me] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("회원 탈퇴 중 문제가 발생했습니다."));
        }
    }
}
