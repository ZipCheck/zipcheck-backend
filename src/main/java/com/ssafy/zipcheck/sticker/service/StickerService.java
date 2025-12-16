package com.ssafy.zipcheck.sticker.service;

import com.ssafy.zipcheck.sticker.dto.StickerCreateRequest;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;

import java.util.List;

public interface StickerService {
    void createSticker(Integer userId, StickerCreateRequest request);
    List<StickerResponse> getStickersByDealId(long dealId);
    void deleteSticker(Integer userId, long stickerId);
}
