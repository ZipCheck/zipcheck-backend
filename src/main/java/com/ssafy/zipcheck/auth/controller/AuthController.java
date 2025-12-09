package com.ssafy.zipcheck.auth.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.auth.dto.LoginResponse;
import com.ssafy.zipcheck.auth.jwt.JwtUtil;
import com.ssafy.zipcheck.auth.dto.LoginRequest;
import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.auth.service.AuthService;
import com.ssafy.zipcheck.users.vo.User;
import com.ssafy.zipcheck.common.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody SignupRequest request) {
        try {
            authService.signup(request);
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
            User user = authService.login(request.getEmail(), request.getPassword());
            String role = user.getRole();

            String access = jwtUtil.createAccessToken(user.getEmail(), role);
            String refresh = jwtUtil.createRefreshToken(user.getEmail(), role);

            // refresh token DB 저장 (email 기반)
            authService.saveRefreshToken(user.getEmail(), refresh);

            // 쿠키 저장
            Cookie cookie = new Cookie("refresh", refresh);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Header로 AccessToken 전달
            response.setHeader("Authorization", "Bearer " + access);

            // 프론트에 보낼 유저 정보 + access token
            LoginResponse loginResponse = new LoginResponse(
                    user.getUserId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getRole(),
                    access
            );

            return ApiResponse.ok(loginResponse);

        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            log.info("로그인 중 에러: {}", e.getMessage());
            return ApiResponse.internalError("로그인 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/reissue")
    public ApiResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        try {
            // 1) 쿠키에서 refresh token 가져오기
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return ApiResponse.badRequest("리프레시 토큰이 없습니다.");
            }

            String refresh = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("refresh")) {
                    refresh = c.getValue();
                }
            }

            if (refresh == null) {
                return ApiResponse.badRequest("리프레시 토큰이 없습니다.");
            }

            // 2) 리프레시 토큰 만료 체크
            try {
                jwtUtil.validateToken(refresh);
            } catch (ExpiredJwtException e) {
                return ApiResponse.badRequest("리프레시 토큰이 만료되었습니다. 다시 로그인하세요.");
            }

            // 3) category 확인 (refresh 토큰인지)
            String category = jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                return ApiResponse.badRequest("올바른 리프레시 토큰이 아닙니다.");
            }

            // 4) 토큰에서 email, role 추출
            String email = jwtUtil.getUsername(refresh);
            String role = jwtUtil.getRole(refresh);

            // 5) DB의 refresh token과 비교
            String saved = authService.findRefreshToken(email);

            if (!saved.equals(refresh)) {
                return ApiResponse.badRequest("리프레시 토큰이 유효하지 않습니다.");
            }

            // 6) 새 토큰 발급
            String newAccess = jwtUtil.createAccessToken(email, role);
            String newRefresh = jwtUtil.createRefreshToken(email, role);

            // 7) DB의 refresh 토큰 갱신
            authService.saveRefreshToken(email, newRefresh);

            // 8) HttpOnly 쿠키로 재저장
            Cookie cookie = new Cookie("refresh", newRefresh);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // 9) access token 헤더로 전달
            response.setHeader("Authorization", "Bearer " + newAccess);

            return ApiResponse.ok("토큰 재발급 완료");

        } catch (Exception e) {
            return ApiResponse.internalError("토큰 재발급 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            // JWT Filter에서 인증된 사용자 정보 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String email = userDetails.getUsername();

            // 1) DB의 refresh token 삭제
            authService.logout(email);

            // 2) refresh 쿠키 무효화
            Cookie cookie = new Cookie("refresh", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 즉시 삭제
            response.addCookie(cookie);

            return ApiResponse.ok("로그아웃 완료");

        } catch (Exception e) {
            return ApiResponse.internalError("로그아웃 중 오류가 발생했습니다.");
        }
    }

}
