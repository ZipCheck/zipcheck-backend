package com.ssafy.zipcheck.notice.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> list() {
        return ResponseEntity.ok(
                ApiResponse.ok(noticeService.getNotices())
        );
    }

    /**
     * 공지사항 상세
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<?>> detail(@PathVariable int noticeId) {
        return ResponseEntity.ok(
                ApiResponse.ok(noticeService.getNotice(noticeId))
        );
    }

    /**
     * 공지사항 등록 (관리자)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NoticeCreateRequest request
    ) {
        if (userDetails == null || !userDetails.getUser().getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.forbidden("관리자만 등록할 수 있습니다."));
        }

        noticeService.createNotice(
                userDetails.getUser().getUserId(),
                request
        );

        return ResponseEntity.ok(ApiResponse.ok("공지사항 등록 완료"));
    }
}
