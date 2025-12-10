package com.ssafy.zipcheck.auth.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.auth.dto.*;
import com.ssafy.zipcheck.auth.jwt.JwtUtil;
import com.ssafy.zipcheck.auth.service.AuthService;
import com.ssafy.zipcheck.users.vo.User;
import com.ssafy.zipcheck.common.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // ============================================================
    // 회원가입
    // ============================================================
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignupRequest request) {
        try {
            authService.signup(request);
            return ResponseEntity.ok(ApiResponse.ok());
        } catch (IllegalArgumentException e) {
            log.warn("[POST /auth/signup] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("회원가입 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[POST /auth/signup] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("회원가입 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 로그인
    // ============================================================
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        try {
            User user = authService.login(request.getEmail(), request.getPassword());
            String role = user.getRole();

            String access = jwtUtil.createAccessToken(user.getEmail(), role);
            String refresh = jwtUtil.createRefreshToken(user.getEmail(), role);

            authService.saveRefreshToken(user.getEmail(), refresh);

            Cookie cookie = new Cookie("refresh", refresh);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            response.setHeader("Authorization", "Bearer " + access);

            LoginResponse loginResponse = new LoginResponse(
                    user.getUserId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getRole(),
                    access
            );

            return ResponseEntity.ok(ApiResponse.ok(loginResponse));

        } catch (IllegalArgumentException e) {
            log.warn("[POST /auth/login] 로그인 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("이메일 또는 비밀번호가 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[POST /auth/login] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("로그인 처리 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 토큰 재발급
    // ============================================================
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reissue(
            HttpServletRequest request, HttpServletResponse response) {

        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("리프레시 토큰이 없습니다."));
            }

            String refresh = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("refresh")) refresh = c.getValue();
            }

            if (refresh == null) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("리프레시 토큰이 없습니다."));
            }

            // 토큰 검증
            try { jwtUtil.validateToken(refresh); }
            catch (ExpiredJwtException e) {
                log.warn("[POST /auth/reissue] Refresh Token 만료");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("리프레시 토큰이 만료되었습니다."));
            }

            if (!jwtUtil.getCategory(refresh).equals("refresh")) {
                log.warn("[POST /auth/reissue] 토큰 카테고리 불일치");
                return ResponseEntity.badRequest()
                        .body(ApiResponse.invalid("올바른 리프레시 토큰이 아닙니다."));
            }

            String email = jwtUtil.getUsername(refresh);
            String role = jwtUtil.getRole(refresh);
            String saved = authService.findRefreshToken(email);

            if (!refresh.equals(saved)) {
                log.warn("[POST /auth/reissue] 저장된 Refresh Token 불일치");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("리프레시 토큰이 유효하지 않습니다."));
            }

            String newAccess = jwtUtil.createAccessToken(email, role);
            String newRefresh = jwtUtil.createRefreshToken(email, role);
            authService.saveRefreshToken(email, newRefresh);

            Cookie cookie = new Cookie("refresh", newRefresh);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            response.setHeader("Authorization", "Bearer " + newAccess);

            return ResponseEntity.ok(ApiResponse.ok("토큰이 재발급되었습니다."));

        } catch (Exception e) {
            log.error("[POST /auth/reissue] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("토큰 재발급 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 로그아웃
    // ============================================================
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(
            HttpServletRequest request, HttpServletResponse response) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String email = userDetails.getUsername();

            authService.logout(email);

            Cookie cookie = new Cookie("refresh", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            return ResponseEntity.ok(ApiResponse.ok("로그아웃 완료"));

        } catch (Exception e) {
            log.error("[POST /auth/logout] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("로그아웃 처리 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 비밀번호 초기화 요청
    // ============================================================
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<?>> requestPasswordReset(
            @RequestBody PasswordResetEmailRequest req) {

        try {
            authService.sendResetPasswordMail(req.getEmail());
            return ResponseEntity.ok(ApiResponse.ok("이메일로 인증 코드가 발송되었습니다."));

        } catch (IllegalArgumentException e) {
            log.warn("[POST /auth/password/reset] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("비밀번호 초기화 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[POST /auth/password/reset] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("비밀번호 초기화 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 비밀번호 초기화 확인
    // ============================================================
    @PostMapping("/password/reset-confirm")
    public ResponseEntity<ApiResponse<?>> confirmPasswordReset(
            @RequestBody PasswordResetConfirmRequest req) {

        try {
            authService.resetPassword(req.getEmail(), req.getCode());
            return ResponseEntity.ok(ApiResponse.ok("임시 비밀번호가 이메일로 전송되었습니다."));

        } catch (IllegalArgumentException e) {
            log.warn("[POST /auth/password/reset-confirm] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("비밀번호 초기화 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[POST /auth/password/reset-confirm] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("비밀번호 초기화 처리 중 문제가 발생했습니다."));
        }
    }
}
