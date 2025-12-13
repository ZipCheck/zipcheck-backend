package com.ssafy.zipcheck.interests.controller;

import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.interests.dto.InterestCreateRequest;
import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;
import com.ssafy.zipcheck.interests.service.InterestService;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getInterests(@AuthenticationPrincipal(expression = "user") User user,
                                                     @ModelAttribute InterestQueryRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.badRequest("로그인이 필요합니다."));
        }
        
        // userId is automatically set in service, but we can pass it explicitly for clarity or initial validation
        Map<String, Object> result = interestService.getInterests(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createInterest(@AuthenticationPrincipal(expression = "user") User user,
                                                       @RequestBody InterestCreateRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.badRequest("로그인이 필요합니다."));
        }
        try {
            interestService.createInterest(user.getUserId(), request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created("관심 매물 등록 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict for duplicate
                    .body(ApiResponse.conflict(e.getMessage()));
        }
    }

    @DeleteMapping("/{interestId}")
    public ResponseEntity<ApiResponse<?>> deleteInterest(@AuthenticationPrincipal(expression = "user") User user,
                                                       @PathVariable Integer interestId) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.badRequest("로그인이 필요합니다."));
        }
        interestService.deleteInterest(user.getUserId(), interestId);
        return ResponseEntity.ok(ApiResponse.ok("관심 매물 삭제 성공", null));
    }
}
