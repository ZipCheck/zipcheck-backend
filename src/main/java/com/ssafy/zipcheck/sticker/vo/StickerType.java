package com.ssafy.zipcheck.sticker.vo;

import lombok.Data;

@Data
public class StickerType {
    private Integer stickerTypeId;
    private String name;
    private String description;
    private String iconUrl;
}
