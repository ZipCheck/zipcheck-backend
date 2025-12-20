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
    @JsonProperty("deal_id")
    private int dealId;

    @JsonProperty("stickers")
    private List<StickerDataDto> stickers;
}
