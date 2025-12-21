package com.ssafy.zipcheck.deals.mapper;

import com.ssafy.zipcheck.deals.dto.MapApartmentResponse;
import com.ssafy.zipcheck.deals.dto.MapClusterResponse;
import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapMapper {
    List<MapDealResponse> searchHouseDeals(MapSearchRequest request);
    List<MapApartmentResponse> searchHouseGroupedByApt(MapSearchRequest request);
    Long countHouseGroupedByApt(MapSearchRequest request);
    List<MapDealResponse> searchHouseDealsByApartmentSeq(MapSearchRequest request); // New method
    Long countHouseDealsByApartmentSeq(MapSearchRequest request); // New method
    List<MapClusterResponse> searchHouseClusters(MapSearchRequest request);
    Long countSearchHouseDeals(MapSearchRequest request);
    MapDealResponse getDealById(long id);
    List<String> getSidoNames();
    List<String> getGugunNames(String sidoName);
    List<String> getDongNames(String sidoName, String gugunName);
}
