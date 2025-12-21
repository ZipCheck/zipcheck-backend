package com.ssafy.zipcheck.deals.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MapSearchResponse<T> {
    private List<T> data;
    private Integer currentPage;
    private Integer totalPages;
    private Long totalCount;
}
