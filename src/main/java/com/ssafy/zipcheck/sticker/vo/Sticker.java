package com.ssafy.zipcheck.sticker.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Sticker {
    private Long stickerId;
    private Integer userId;
    private Integer dealId;
    private Integer stickerTypeId;
    private String description;
    private LocalDateTime createdAt;
}
