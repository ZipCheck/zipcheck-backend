package com.ssafy.zipcheck.interests.dto;

import lombok.Data;

@Data
public class InterestQueryRequest {
    // For identifying the user
    private Integer userId;

    // Filtering options
    private String sidoName;
    private String gugunName;
    private String dongName;
    private Double minArea;
    private Double maxArea;
    private Long minPrice;
    private Long maxPrice;

    // Sorting options (can be added later, e.g., "dealDate_desc", "area_asc")
    // private String sortBy;

    // Pagination options
    private int size = 3; // Default to 3 items per page as requested
    private int offset;
}
