package com.ssafy.zipcheck.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDataRequestDto {
    @JsonProperty("apt_id")
    private String aptId; // Changed from dealId (int)

    @JsonProperty("stickers")
    private List<StickerDataDto> stickers;
}
