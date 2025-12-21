package com.ssafy.zipcheck.deals.mapper;

import com.ssafy.zipcheck.deals.dto.MapClusterResponse;
import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapMapper {
    List<MapDealResponse> searchHouseDeals(MapSearchRequest request);
    List<MapClusterResponse> searchHouseClusters(MapSearchRequest request);
    Long countSearchHouseDeals(MapSearchRequest request);
    MapDealResponse getDealById(long id);
    List<String> getSidoNames();
    List<String> getGugunNames(String sidoName);
    List<String> getDongNames(String sidoName, String gugunName);
}
