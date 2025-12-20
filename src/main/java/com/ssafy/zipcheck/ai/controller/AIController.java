package com.ssafy.zipcheck.ai.controller;

import com.ssafy.zipcheck.ai.dto.ChatbotResponseDto;
import com.ssafy.zipcheck.ai.dto.IndexRequest;
import com.ssafy.zipcheck.ai.service.AIService;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final WebClient webClient; // For requestIndexing

    @GetMapping("/deals/{dealId}/summary")
    public ResponseEntity<ApiResponse<ChatbotResponseDto>> getStickerSummary(@PathVariable Long dealId) {
        try {
            ChatbotResponseDto response = aiService.getSummaryOfStickers(dealId);
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (Exception e) {
            log.error("[GET /api/ai/deals/{}/summary] Error summarizing stickers: {}", dealId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("스티커 요약 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/index-reviews")
    public ResponseEntity<ApiResponse<?>> requestIndexing(@RequestBody IndexRequest request) {
        try {
            aiService.requestIndexingWithData(request.getDealId());
            return ResponseEntity.accepted().body(ApiResponse.ok("인덱싱 요청이 성공적으로 접수되었습니다."));
        } catch (Exception e) {
            log.error("Error during indexing request for dealId: {}", request.getDealId(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("인덱싱 요청 처리 중 오류가 발생했습니다."));
        }
    }
}
