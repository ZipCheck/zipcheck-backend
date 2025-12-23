package com.ssafy.zipcheck.sticker.dto;

import lombok.Data;

@Data
public class StickerMapRow {
    private String aptId;
    private Double latitude;
    private Double longitude;
    private Integer stickerTypeId;
    private String emoji;
    private Long count;
}
