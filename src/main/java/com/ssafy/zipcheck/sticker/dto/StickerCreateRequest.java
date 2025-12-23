package com.ssafy.zipcheck.sticker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StickerCreateRequest {
    private String aptId; // Changed from dealId (Integer)
    private String sentiment;
    private String emoticon;
    private String description;
}

