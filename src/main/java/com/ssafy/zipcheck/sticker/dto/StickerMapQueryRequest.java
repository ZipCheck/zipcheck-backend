package com.ssafy.zipcheck.sticker.dto;

import lombok.Data;

@Data
public class StickerMapQueryRequest {
    private Double minLatitude;
    private Double maxLatitude;
    private Double minLongitude;
    private Double maxLongitude;
}
