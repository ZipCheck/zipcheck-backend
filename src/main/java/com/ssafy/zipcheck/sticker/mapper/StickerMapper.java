package com.ssafy.zipcheck.sticker.mapper;

import com.ssafy.zipcheck.sticker.dto.StickerMapQueryRequest;
import com.ssafy.zipcheck.sticker.dto.StickerMapRow;
import com.ssafy.zipcheck.sticker.dto.StickerResponse;
import com.ssafy.zipcheck.sticker.vo.Sticker;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StickerMapper {

    void insertSticker(Sticker sticker);

    Optional<Sticker> findStickerById(long stickerId);

    List<StickerResponse> findStickersByAptId(String aptId);

    void deleteSticker(long stickerId);

    Integer findUserIdByStickerId(long stickerId);

    Integer findStickerTypeIdByName(String name);

    List<StickerMapRow> findStickerEmotionsByBounds(StickerMapQueryRequest request);
}
