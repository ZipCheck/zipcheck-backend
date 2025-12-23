package com.ssafy.zipcheck.sticker.dto;

import lombok.Data;

import java.util.List;

@Data
public class StickerMapResponse {
    private String aptId;
    private Double latitude;
    private Double longitude;
    private List<StickerEmotionCount> emotions;
}
