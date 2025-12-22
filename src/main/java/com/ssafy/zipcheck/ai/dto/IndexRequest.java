package com.ssafy.zipcheck.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IndexRequest {
    @JsonProperty("deal_id")
    private int dealId;
}
