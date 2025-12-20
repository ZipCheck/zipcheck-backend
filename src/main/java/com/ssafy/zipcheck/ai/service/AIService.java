package com.ssafy.zipcheck.ai.service;

import com.ssafy.zipcheck.ai.dto.ChatbotResponseDto;

public interface AIService {
    ChatbotResponseDto getSummaryOfStickers(Long dealId);
    void requestIndexingWithData(int dealId);
}
