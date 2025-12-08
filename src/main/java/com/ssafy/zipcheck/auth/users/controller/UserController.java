package com.ssafy.zipcheck.auth.users.controller;

import com.ssafy.zipcheck.auth.security.jwt.JwtUtil;
import com.ssafy.zipcheck.auth.users.dto.LoginRequest;
import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.service.UserService;
import com.ssafy.zipcheck.auth.users.vo.User;
import com.ssafy.zipcheck.common.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody SignupRequest request) {
        try {
            userService.signup(request);
            return ApiResponse.ok();
        } catch (IllegalArgumentException e) {
            log.info("회원가입 잘못된 요청: {}", e.getMessage());
            return ApiResponse.badRequest("잘못된 요청");
        } catch (Exception e) {
            log.info("회원가입 서버 오류: {}", e.getMessage());
            return ApiResponse.internalError("회원가입 중 서버 오류 발생");
        }
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request,
                                HttpServletResponse response) {

        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            String role = user.getRole();

            String access = jwtUtil.createAccessToken(user.getEmail(), role);
            String refresh = jwtUtil.createRefreshToken(user.getEmail(), role);

            // refresh token DB 저장 (email 기반)
            userService.saveRefreshToken(user.getEmail(), refresh);

            // 쿠키 저장
            Cookie cookie = new Cookie("refresh", refresh);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Header로 AccessToken 전달
            response.setHeader("Authorization", "Bearer " + access);

            return ApiResponse.ok("로그인 성공");

        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            log.info("로그인 중 에러: {}", e.getMessage());
            return ApiResponse.internalError("로그인 중 오류가 발생했습니다.");
        }
    }


    /**
     * 비밀번호 변경
     */
    @PatchMapping("/password")
    public ApiResponse<Void> updatePassword(@RequestBody UpdatePasswordRequest request) {
        try {
            userService.updatePassword(request);
            return ApiResponse.ok();
        } catch (IllegalArgumentException e) {
            log.info("비밀번호 변경 잘못된 요청: {}", e.getMessage());
            return ApiResponse.badRequest("잘못된 요청");
        } catch (Exception e) {
            log.info("비밀번호 변경 서버 오류: {}", e.getMessage());
            return ApiResponse.internalError("비밀번호 변경 중 서버 오류 발생");
        }
    }
}
