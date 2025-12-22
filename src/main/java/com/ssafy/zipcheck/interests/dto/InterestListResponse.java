package com.ssafy.zipcheck.interests.dto;

import lombok.Data;
import java.util.List;

@Data
public class InterestListResponse {
    private List<InterestResponse> items;
    private int totalCount;
    private int page;
    private int size;
}
