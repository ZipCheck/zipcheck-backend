package com.ssafy.zipcheck.interests.service;

import com.ssafy.zipcheck.interests.dto.InterestListResponse;
import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;

public interface InterestService {

    /**
     * 관심 매물 목록 조회
     */
    InterestListResponse getInterests(
            Integer userId,
            InterestQueryRequest request
    );

    /**
     * 관심 매물 등록
     */
    void createInterest(
            Integer userId,
            Integer dealNo
    );

    /**
     * 관심 매물 삭제
     */
    void deleteInterest(
            Integer userId,
            Integer dealNo
    );
}
