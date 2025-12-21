package com.ssafy.zipcheck.deals.dto;

import lombok.Data;

@Data
public class MapClusterResponse {
    private String regionName;
    private Double latitude;
    private Double longitude;
    private Long dealCount;
}
