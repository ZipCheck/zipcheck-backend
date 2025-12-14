package com.ssafy.zipcheck.sticker.mapper;

import com.ssafy.zipcheck.sticker.dto.StickerResponse;
import com.ssafy.zipcheck.sticker.vo.Sticker;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StickerMapper {

    void insertSticker(Sticker sticker);

    Optional<Sticker> findStickerById(long stickerId);

    List<StickerResponse> findStickersByDealId(long dealId);

    void deleteSticker(long stickerId);

    Integer findUserIdByStickerId(long stickerId);
}