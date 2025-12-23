package com.ssafy.zipcheck.sticker.service;

import com.ssafy.zipcheck.sticker.dto.StickerCreateRequest;
import com.ssafy.zipcheck.sticker.dto.StickerMapQueryRequest;
import com.ssafy.zipcheck.sticker.dto.StickerMapResponse;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;

import java.util.List;

public interface StickerService {
    void createSticker(Integer userId, StickerCreateRequest request);
    List<StickerResponse> getStickersByAptId(String aptId); // Renamed method and changed parameter type
    void deleteSticker(Integer userId, long stickerId);
    List<StickerMapResponse> getStickerEmotionsByBounds(StickerMapQueryRequest request);
}
