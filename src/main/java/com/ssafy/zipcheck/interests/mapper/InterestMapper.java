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
    void deleteInterest(@Param("interestId") Integer interestId, @Param("userId") Integer userId);
    List<InterestResponse> findInterestsByUser(InterestQueryRequest request);
    int countInterestsByUser(InterestQueryRequest request);
    Interest findInterestByUserIdAndDealNo(@Param("userId") Integer userId, @Param("dealNo") Integer dealNo);
}
