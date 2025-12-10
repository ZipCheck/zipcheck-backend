package com.ssafy.zipcheck.deals.service;

import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;
import com.ssafy.zipcheck.deals.mapper.MapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final MapMapper mapMapper;

    @Override
    public List<MapDealResponse> searchHouseDeals(MapSearchRequest request) {
        return mapMapper.searchHouseDeals(request);
    }

    @Override
    public MapDealResponse getDealById(long id) {
        return mapMapper.getDealById(id);
    }

    @Override
    public List<String> getSidoNames() {
        return mapMapper.getSidoNames();
    }

    @Override
    public List<String> getGugunNames(String sidoName) {
        return mapMapper.getGugunNames(sidoName);
    }

    @Override
    public List<String> getDongNames(String sidoName, String gugunName) {
        return mapMapper.getDongNames(sidoName, gugunName);
    }
}
