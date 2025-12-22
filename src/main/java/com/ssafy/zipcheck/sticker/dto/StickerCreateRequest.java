package com.ssafy.zipcheck.sticker.dto;

import com.ssafy.zipcheck.sticker.vo.Sticker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StickerCreateRequest {
    private Integer dealId;
    private Integer stickerTypeId;
    private String description;

    public Sticker toEntity(Integer userId) {
        Sticker sticker = new Sticker();
        sticker.setUserId(userId);
        sticker.setDealId(this.dealId);
        sticker.setStickerTypeId(this.stickerTypeId);
        sticker.setDescription(this.description);
        return sticker;
    }
}
