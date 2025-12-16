package com.ssafy.zipcheck.sticker.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter // For MyBatis mapping
@ToString // For debugging
public class StickerResponse {
    private Long stickerId;
    private Integer userId;
    private String userNickname;
    private Integer dealId;
    private Integer stickerTypeId;
    private String stickerTypeName;
    private String stickerIconUrl;
    private String description;
    private LocalDateTime createdAt;
}
