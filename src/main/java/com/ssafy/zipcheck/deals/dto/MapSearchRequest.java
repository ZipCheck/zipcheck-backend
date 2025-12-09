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

    // Pagination
    private int page = 1;  // 기본 페이지 번호
    private int size = 20; // 페이지당 기본 데이터 개수

    public int getOffset() {
        return (page > 0) ? (page - 1) * size : 0;
    }
}
