package com.ssafy.zipcheck.interests.service;

import com.ssafy.zipcheck.interests.dto.InterestCreateRequest;
import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;
import com.ssafy.zipcheck.interests.dto.InterestResponse;
import com.ssafy.zipcheck.common.response.ApiResponse; // Assuming ApiResponse is in common

import java.util.List;
import java.util.Map;

public interface InterestService {
    Map<String, Object> getInterests(Integer userId, InterestQueryRequest request);
    void createInterest(Integer userId, InterestCreateRequest request);
    void deleteInterest(Integer userId, Integer interestId);
}
