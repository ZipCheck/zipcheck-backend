package com.ssafy.zipcheck.interests.controller;

import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.interests.dto.*;
import com.ssafy.zipcheck.interests.service.InterestService;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getInterests(
            @AuthenticationPrincipal(expression = "user") User user,
            @ModelAttribute InterestQueryRequest request
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        InterestListResponse response =
                interestService.getInterests(user.getUserId(), request);

        System.out.println("=======================");
        System.out.println("response : " + response);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/{dealNo}")
    public ResponseEntity<ApiResponse<?>> createInterest(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable Integer dealNo
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        interestService.createInterest(user.getUserId(), dealNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("관심 매물 등록 성공"));
    }

    @DeleteMapping("/{dealNo}")
    public ResponseEntity<ApiResponse<?>> deleteInterest(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable Integer dealNo
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        interestService.deleteInterest(user.getUserId(), dealNo);
        return ResponseEntity.ok(ApiResponse.ok("관심 매물 삭제 성공", null));
    }
}
