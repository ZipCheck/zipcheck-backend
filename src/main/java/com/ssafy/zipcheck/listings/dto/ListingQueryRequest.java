package com.ssafy.zipcheck.listings.dto;

import lombok.Data;

@Data
public class ListingQueryRequest {

    private String sidoName;
    private String gugunName;
    private String dongName;

    private Double minArea;
    private Double maxArea;

    private Long minPrice;
    private Long maxPrice;

    // pagination
    private int page = 1;
    private int size = 10;

    // 내부 계산용
    private int offset;
}
