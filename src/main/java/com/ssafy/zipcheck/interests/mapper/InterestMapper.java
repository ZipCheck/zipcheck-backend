package com.ssafy.zipcheck.interests.mapper;

import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;
import com.ssafy.zipcheck.interests.dto.InterestResponse;
import com.ssafy.zipcheck.interests.vo.Interest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InterestMapper {

    void addInterest(Interest interest);

    void deleteByUserAndDeal(
            @Param("userId") Integer userId,
            @Param("dealNo") Integer dealNo
    );

    Interest findByUserIdAndDealNo(
            @Param("userId") Integer userId,
            @Param("dealNo") Integer dealNo
    );

    List<InterestResponse> findInterestsByUser(
            @Param("userId") Integer userId,
            @Param("request") InterestQueryRequest request
    );

    int countInterestsByUser(
            @Param("userId") Integer userId,
            @Param("request") InterestQueryRequest request
    );
}
