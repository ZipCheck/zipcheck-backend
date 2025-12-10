package com.ssafy.zipcheck.deals.service;

import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;

import java.util.List;

public interface MapService {
    List<MapDealResponse> searchHouseDeals(MapSearchRequest request);
    MapDealResponse getDealById(long id);
    List<String> getSidoNames();
    List<String> getGugunNames(String sidoName);
    List<String> getDongNames(String sidoName, String gugunName);
}
