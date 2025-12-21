package com.ssafy.zipcheck.deals.service;

import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;
import com.ssafy.zipcheck.deals.dto.MapSearchResponse;

import java.util.List;

public interface MapService {
    MapSearchResponse<?> searchHouseDeals(MapSearchRequest request);
    MapDealResponse getDealById(long id);
    List<String> getSidoNames();
    List<String> getGugunNames(String sidoName);
    List<String> getDongNames(String sidoName, String gugunName);
}
