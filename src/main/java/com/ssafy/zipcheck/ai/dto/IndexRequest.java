package com.ssafy.zipcheck.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IndexRequest {
    @JsonProperty("apt_id")
    private String aptId; // Changed from dealId (int)
}
