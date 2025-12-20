package com.ssafy.zipcheck.ai.service;

import com.ssafy.zipcheck.ai.dto.ChatbotResponseDto;
import com.ssafy.zipcheck.ai.dto.IndexDataRequestDto;
import com.ssafy.zipcheck.ai.dto.StickerDataDto;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;
import com.ssafy.zipcheck.sticker.service.StickerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {

    private final WebClient webClient;
    private final StickerService stickerService;

    @Override
    public ChatbotResponseDto getSummaryOfStickers(Long dealId) {
        log.info("Requesting sticker summary for dealId: {}", dealId);

        String question = "이 매물에 대한 주민들의 스티커 리뷰들을 종합적으로 요약해줘.";

        // Python AI 서버의 "POST /api/deals/{deal_id}/chatbot" 엔드포인트 호출
        return webClient.post()
                .uri("/api/deals/" + dealId + "/chatbot")
                .body(Mono.just(Map.of("question", question)), Map.class)
                .retrieve()
                .bodyToMono(ChatbotResponseDto.class)
                .doOnSuccess(response -> log.info("Successfully summarized stickers for dealId {}: {}", dealId, response))
                .doOnError(error -> log.error("Error while summarizing stickers for dealId {}: {}", dealId, error.getMessage()))
                .block();
    }

    @Override
    public void requestIndexingWithData(int dealId) {
        log.info("Preparing data for indexing for dealId: {}", dealId);

        // 1. StickerService를 통해 스티커 데이터 조회
        List<StickerResponse> stickers = stickerService.getStickersByDealId(dealId);

        // 2. DTO로 변환
        List<StickerDataDto> stickerDataDtos = stickers.stream()
                .map(sticker -> StickerDataDto.builder()
                        .stickerId(sticker.getStickerId())
                        .description(sticker.getDescription())
                        .build())
                .collect(Collectors.toList());

        IndexDataRequestDto requestDto = IndexDataRequestDto.builder()
                .dealId(dealId)
                .stickers(stickerDataDtos)
                .build();

        // 3. WebClient로 Python 서버의 새 엔드포인트 호출
        webClient.post()
                .uri("/api/index-reviews-with-data")
                .body(Mono.just(requestDto), IndexDataRequestDto.class)
                .retrieve()
                .bodyToMono(String.class) // 응답 바디를 String으로 받음
                .doOnSuccess(response -> log.info("Successfully requested indexing for dealId: {}. Response: {}", dealId, response))
                .doOnError(error -> log.error("Error while requesting indexing for dealId: {}", dealId, error))
                .subscribe(); // 비동기로 요청을 보내고 바로 리턴
    }
}
