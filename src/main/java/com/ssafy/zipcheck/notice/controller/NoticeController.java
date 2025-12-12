package com.ssafy.zipcheck.notice.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.dto.NoticeUpdateRequest;
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

    @GetMapping
    public ResponseEntity<ApiResponse<?>> list() {
        return ResponseEntity.ok(
                ApiResponse.ok(noticeService.getNotices())
        );
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<?>> detail(@PathVariable int noticeId) {
        return ResponseEntity.ok(
                ApiResponse.ok(noticeService.getNotice(noticeId))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NoticeCreateRequest request
    ) {
        noticeService.createNotice(
                userDetails.getUser().getUserId(),
                request
        );
        return ResponseEntity.ok(ApiResponse.ok("공지사항 등록 완료"));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable int noticeId,
            @RequestBody NoticeUpdateRequest request
    ) {
        noticeService.updateNotice(noticeId, request);
        return ResponseEntity.ok(ApiResponse.ok("공지사항 수정 완료"));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable int noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(ApiResponse.ok("공지사항 삭제 완료"));
    }
}
