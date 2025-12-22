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
    public ChatbotResponseDto getApartmentReport(String aptSeq) {
        log.info("Requesting AI report for aptSeq: {}", aptSeq);

        // 1. aptSeq에 해당하는 모든 스티커 데이터 조회
        List<StickerResponse> stickers = stickerService.getStickersByAptSeq(aptSeq);

        // 2. DTO로 변환
        List<StickerDataDto> stickerDataDtos = stickers.stream()
                .map(sticker -> StickerDataDto.builder()
                        .stickerId(sticker.getStickerId())
                        .description(sticker.getDescription())
                        .build())
                .collect(Collectors.toList());

        // 3. AI 서버에 보낼 질문 및 데이터 구성
        String question = aptSeq + " 아파트 단지에 대한 주민들의 스티커 리뷰들을 종합적으로 요약하고, 전반적인 거주 환경에 대한 보고서를 작성해줘.";
        
        Map<String, Object> requestBody = Map.of(
            "apt_seq", aptSeq,
            "question", question,
            "stickers", stickerDataDtos
        );

        // Python AI 서버의 새로운 엔드포인트 호출 (예: /api/apartments/{apt_seq}/chatbot)
        // 여기서는 임시로 /api/chatbot 으로 보내고 apt_seq를 body에 포함
        return webClient.post()
                .uri("/api/apartments/" + aptSeq + "/chatbot") // AI 서버의 새로운 엔드포인트
                .body(Mono.just(requestBody), Map.class)
                .retrieve()
                .bodyToMono(ChatbotResponseDto.class)
                .doOnSuccess(response -> log.info("Successfully received AI report for aptSeq {}: {}", aptSeq, response))
                .doOnError(error -> log.error("Error while getting AI report for aptSeq {}: {}", aptSeq, error.getMessage()))
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
