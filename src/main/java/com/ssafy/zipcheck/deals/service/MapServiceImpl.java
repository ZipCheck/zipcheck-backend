package com.ssafy.zipcheck.deals.service;

import com.ssafy.zipcheck.deals.dto.MapApartmentResponse;
import com.ssafy.zipcheck.deals.dto.MapClusterResponse;
import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;
import com.ssafy.zipcheck.deals.dto.MapSearchResponse;
import com.ssafy.zipcheck.deals.mapper.MapMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final MapMapper mapMapper;
    private static final int CLUSTER_ZOOM_THRESHOLD = 10; // 예시: 줌 레벨 10 이하일 때 클러스터링

    @Override
    public MapSearchResponse<?> searchHouseDeals(MapSearchRequest request) {
        // Handle nulls for page and size with defaults
        int page = request.getPage() != null ? request.getPage() : 1;
        int size = request.getSize() != null ? request.getSize() : 20;
        request.setSize(size); // Ensure size is set in request for mapper usage

        // Calculate offset for pagination
        int offset = (page - 1) * size;
        request.setPage(offset); // Temporarily store offset in page field for mapper use (LIMIT #{size} OFFSET #{page})

        if (request.getZoomLevel() != null && request.getZoomLevel() <= CLUSTER_ZOOM_THRESHOLD) {
            List<MapClusterResponse> clusters = mapMapper.searchHouseClusters(request);
            int clusterSize = clusters != null ? clusters.size() : 0;
            
            return MapSearchResponse.<MapClusterResponse>builder()
                    .data(clusters)
                    .totalCount((long) clusterSize) 
                    .currentPage(1)
                    .totalPages(1)
                    .build();
        } else {
            // Grouped by Apartment Complex (instead of individual deals)
            List<MapApartmentResponse> apartments = mapMapper.searchHouseGroupedByApt(request);
            Long totalCount = mapMapper.countHouseGroupedByApt(request);
            
            int totalPages = (int) Math.ceil((double) totalCount / size);

            return MapSearchResponse.<MapApartmentResponse>builder()
                    .data(apartments)
                    .totalCount(totalCount)
                    .currentPage(page) 
                    .totalPages(totalPages)
                    .build();
        }
    }

    @Override
    public MapDealResponse getDealById(long id) {
        return mapMapper.getDealById(id);
    }

    @Override
    public MapSearchResponse<MapDealResponse> getDealsByApartmentSeq(MapSearchRequest request) {
        // Handle nulls for page and size with defaults
        int page = request.getPage() != null ? request.getPage() : 1;
        int size = request.getSize() != null ? request.getSize() : 20;
        request.setSize(size);

        // Calculate offset for pagination
        int offset = (page - 1) * size;
        request.setPage(offset); // Temporarily store offset in page field for mapper use

        List<MapDealResponse> deals = mapMapper.searchHouseDealsByApartmentSeq(request); // New mapper method
        Long totalCount = mapMapper.countHouseDealsByApartmentSeq(request); // New mapper method

        int totalPages = (int) Math.ceil((double) totalCount / size);

        return MapSearchResponse.<MapDealResponse>builder()
                .data(deals)
                .totalCount(totalCount)
                .currentPage(page)
                .totalPages(totalPages)
                .build();
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
