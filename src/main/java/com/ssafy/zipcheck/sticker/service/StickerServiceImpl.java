package com.ssafy.zipcheck.sticker.service;

import com.ssafy.zipcheck.sticker.dto.StickerCreateRequest;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;
import com.ssafy.zipcheck.sticker.mapper.StickerMapper;
import com.ssafy.zipcheck.sticker.vo.Sticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.zipcheck.deals.mapper.MapMapper; // MapMapper import 추가

import java.util.ArrayList; // ArrayList import 추가
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StickerServiceImpl implements StickerService {

    private final StickerMapper stickerMapper;
    private final MapMapper mapMapper; // MapMapper 주입
    
    @Override
    @Transactional
    public void createSticker(Integer userId, StickerCreateRequest request) {
        Sticker sticker = request.toEntity(userId);
        stickerMapper.insertSticker(sticker);
    }

    @Override
    public List<StickerResponse> getStickersByDealId(long dealId) {
        return stickerMapper.findStickersByDealId(dealId);
    }

    @Override
    public List<StickerResponse> getStickersByAptSeq(String aptSeq) {
        List<Long> dealIds = mapMapper.findDealIdsByAptSeq(aptSeq);
        List<StickerResponse> allStickers = new ArrayList<>();
        for (Long dealId : dealIds) {
            allStickers.addAll(stickerMapper.findStickersByDealId(dealId));
        }
        return allStickers;
    }

    @Override
    @Transactional
    public void deleteSticker(Integer userId, long stickerId) {
        Integer ownerId = stickerMapper.findUserIdByStickerId(stickerId);

        if (ownerId == null) {
            // Or throw a specific "NotFoundException"
            throw new RuntimeException("Sticker not found with id: " + stickerId);
        }

        if (!Objects.equals(ownerId, userId)) {
            // Or throw a specific "ForbiddenException"
            throw new RuntimeException("User does not have permission to delete this sticker");
        }

        stickerMapper.deleteSticker(stickerId);
    }
}
