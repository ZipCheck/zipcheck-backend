package com.ssafy.zipcheck.sticker.service;

import com.ssafy.zipcheck.sticker.dto.StickerCreateRequest;
import com.ssafy.zipcheck.sticker.dto.StickerEmotionCount;
import com.ssafy.zipcheck.sticker.dto.StickerMapQueryRequest;
import com.ssafy.zipcheck.sticker.dto.StickerMapResponse;
import com.ssafy.zipcheck.sticker.dto.StickerMapRow;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;
import com.ssafy.zipcheck.sticker.mapper.StickerMapper;
import com.ssafy.zipcheck.sticker.vo.Sticker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StickerServiceImpl implements StickerService {

    private final StickerMapper stickerMapper;

    @Override
    @Transactional
    public void createSticker(Integer userId, StickerCreateRequest request) {
        log.debug("{} ", request.getEmoticon());
        Integer stickerTypeId = stickerMapper.findStickerTypeIdByName(request.getEmoticon());
        
        if (stickerTypeId == null) {
            throw new IllegalArgumentException("Invalid emoticon name: " + request.getEmoticon());
        }
        log.debug("aptId is {}", request.getAptId());
        Sticker sticker = new Sticker();
        sticker.setUserId(userId);
        sticker.setAptId(request.getAptId()); // Changed from setDealId
        sticker.setStickerTypeId(stickerTypeId);
        sticker.setDescription(request.getDescription());
        
        stickerMapper.insertSticker(sticker);
    }

    @Override
    public List<StickerResponse> getStickersByAptId(String aptId) { // Renamed from getStickersByDealId
        return stickerMapper.findStickersByAptId(aptId); // Changed mapper method call
    }

    @Override
    public List<StickerMapResponse> getStickerEmotionsByBounds(StickerMapQueryRequest request) {
        if (request.getMinLatitude() == null || request.getMaxLatitude() == null
                || request.getMinLongitude() == null || request.getMaxLongitude() == null) {
            throw new IllegalArgumentException("Latitude and longitude bounds are required.");
        }

        List<StickerMapRow> rows = stickerMapper.findStickerEmotionsByBounds(request);
        Map<String, StickerMapResponse> grouped = new LinkedHashMap<>();

        for (StickerMapRow row : rows) {
            StickerMapResponse response = grouped.computeIfAbsent(row.getAptId(), aptId -> {
                StickerMapResponse mapResponse = new StickerMapResponse();
                mapResponse.setAptId(aptId);
                mapResponse.setLatitude(row.getLatitude());
                mapResponse.setLongitude(row.getLongitude());
                mapResponse.setEmotions(new ArrayList<>());
                return mapResponse;
            });

            response.getEmotions().add(
                    new StickerEmotionCount(row.getStickerTypeId(), row.getEmoji(), row.getCount())
            );
        }

        return new ArrayList<>(grouped.values());
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
