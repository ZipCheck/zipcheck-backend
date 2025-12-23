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

    @GetMapping("/apartments/{aptSeq}/summary") // 엔드포인트 경로 변경 (report -> summary)
    public ResponseEntity<ApiResponse<ChatbotResponseDto>> getApartmentAIReport(@PathVariable String aptSeq) {
        try {
            ChatbotResponseDto response = aiService.getApartmentReport(aptSeq);
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (Exception e) {
            log.error("[GET /api/ai/apartments/{}/summary] Error generating AI report: {}", aptSeq, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("아파트 AI 리포트 생성 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/index-reviews")
    public ResponseEntity<ApiResponse<?>> requestIndexing(@RequestBody IndexRequest request) {
        try {
            aiService.requestIndexingWithData(request.getAptId()); // Changed to getAptId()
            return ResponseEntity.accepted().body(ApiResponse.ok("인덱싱 요청이 성공적으로 접수되었습니다."));
        } catch (Exception e) {
            log.error("Error during indexing request for aptId: {}", request.getAptId(), e); // Changed log message
            return ResponseEntity.internalServerError().body(ApiResponse.internalError("인덱싱 요청 처리 중 오류가 발생했습니다."));
        }
    }
}
