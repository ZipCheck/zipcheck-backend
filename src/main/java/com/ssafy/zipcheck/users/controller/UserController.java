package com.ssafy.zipcheck.users.controller;

import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.security.UserPrincipal;
import com.ssafy.zipcheck.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zipcheck/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.badRequest("Unauthorized"));
        }
        userService.deleteUser(principal.getUserId());
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
