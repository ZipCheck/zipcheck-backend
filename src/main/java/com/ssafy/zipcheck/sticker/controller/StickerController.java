package com.ssafy.zipcheck.sticker.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.sticker.dto.StickerCreateRequest;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;
import com.ssafy.zipcheck.sticker.service.StickerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stickers")
@RequiredArgsConstructor
public class StickerController {

    private final StickerService stickerService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSticker(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StickerCreateRequest request) {

        if (userDetails == null) {
            log.warn("[POST /api/stickers] Unauthorized access attempt.");
            return ResponseEntity.status(401).body(ApiResponse.unauthorized("인증 정보가 필요합니다."));
        }

        try {
            Integer userId = userDetails.getUser().getUserId();
            stickerService.createSticker(userId, request);
            return ResponseEntity.status(201).body(ApiResponse.ok("스티커가 성공적으로 등록되었습니다."));
        } catch (Exception e) {
            log.error("[POST /api/stickers] Error creating sticker: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("스티커 등록 중 오류가 발생했습니다."));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StickerResponse>>> getStickersByDealId(@RequestParam long dealId) {
        try {
            List<StickerResponse> stickers = stickerService.getStickersByDealId(dealId);
            return ResponseEntity.ok(ApiResponse.ok(stickers));
        } catch (Exception e) {
            log.error("[GET /api/stickers?dealId={}] Error fetching stickers: {}", dealId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("스티커 조회 중 오류가 발생했습니다."));
        }
    }

    @DeleteMapping("/{stickerId}")
    public ResponseEntity<ApiResponse<?>> deleteSticker(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long stickerId) {

        if (userDetails == null) {
            log.warn("[DELETE /api/stickers/{}] Unauthorized access attempt.", stickerId);
            return ResponseEntity.status(401).body(ApiResponse.unauthorized("인증 정보가 필요합니다."));
        }

        try {
            Integer userId = userDetails.getUser().getUserId();
            stickerService.deleteSticker(userId, stickerId);
            return ResponseEntity.ok(ApiResponse.ok("스티커가 성공적으로 삭제되었습니다."));
        } catch (RuntimeException e) {
            // This will catch the specific exceptions thrown from the service
            log.warn("[DELETE /api/stickers/{}] Failed to delete sticker: {}", stickerId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.invalid(e.getMessage()));
        } catch (Exception e) {
            log.error("[DELETE /api/stickers/{}] Error deleting sticker: {}", stickerId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("스티커 삭제 중 오류가 발생했습니다."));
        }
    }
}
