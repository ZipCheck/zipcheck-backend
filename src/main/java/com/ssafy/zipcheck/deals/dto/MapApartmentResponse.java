package com.ssafy.zipcheck.deals.dto;

import lombok.Data;

@Data
public class MapApartmentResponse {
    private String aptSeq;
    private String aptName;
    private Double latitude;
    private Double longitude;
    private Long dealCount;
    private String recentDealAmount; // 가장 최근 거래 가격 (대표 가격으로 사용)
}
