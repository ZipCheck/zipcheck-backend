package com.ssafy.zipcheck.listings.mapper;

import com.ssafy.zipcheck.listings.dto.ListingQueryRequest;
import com.ssafy.zipcheck.listings.dto.ListingResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ListingMapper {

    List<ListingResponse> findListings(
            @Param("userId") Integer userId,
            @Param("request") ListingQueryRequest request
    );
}
