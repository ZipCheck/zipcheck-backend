package com.ssafy.zipcheck.auth.users.controller;


import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.service.UserService;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

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
