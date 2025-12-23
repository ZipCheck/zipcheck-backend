package com.ssafy.zipcheck.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StickerEmotionCount {
    private Integer stickerTypeId;
    private String emoji;
    private Long count;
}
