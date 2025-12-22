package com.ssafy.zipcheck.deals.dto;

import lombok.Data;

@Data
public class MapSearchRequest {

    private String sidoName;
    private String gugunName;
    private String dongName;
    private String aptName;
    private Double minArea;
    private Double maxArea;
    private Long minPrice;
    private Long maxPrice;
    private Double minLatitude;
    private Double maxLatitude;
    private Double minLongitude;
    private Double maxLongitude;
    private Integer zoomLevel;
    private Integer page;
    private Integer size;
    private String aptSeq;

    // 로그인 사용자 ID
    private Integer userId;
}
