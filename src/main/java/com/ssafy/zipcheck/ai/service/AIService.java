package com.ssafy.zipcheck.ai.service;

import com.ssafy.zipcheck.ai.dto.ChatbotResponseDto;

public interface AIService {
    ChatbotResponseDto getApartmentReport(String aptSeq); // dealId 대신 aptSeq를 받음
    void requestIndexingWithData(int dealId);
}
