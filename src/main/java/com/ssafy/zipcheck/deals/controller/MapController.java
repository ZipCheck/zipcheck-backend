package com.ssafy.zipcheck.deals.controller;

import com.ssafy.zipcheck.deals.dto.MapDealResponse;
import com.ssafy.zipcheck.deals.dto.MapSearchRequest;
import com.ssafy.zipcheck.deals.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zipcheck/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    public ResponseEntity<List<MapDealResponse>> searchHouseDeals(@ModelAttribute MapSearchRequest request) {
        List<MapDealResponse> deals = mapService.searchHouseDeals(request);
        return ResponseEntity.ok(deals);
    }

    @GetMapping("/sido")
    public ResponseEntity<List<String>> getSidoNames() {
        List<String> sidoNames = mapService.getSidoNames();
        return ResponseEntity.ok(sidoNames);
    }

    @GetMapping("/gugun")
    public ResponseEntity<List<String>> getGugunNames(@RequestParam String sido) {
        List<String> gugunNames = mapService.getGugunNames(sido);
        return ResponseEntity.ok(gugunNames);
    }

    @GetMapping("/dong")
    public ResponseEntity<List<String>> getDongNames(@RequestParam String sido, @RequestParam String gugun) {
        List<String> dongNames = mapService.getDongNames(sido, gugun);
        return ResponseEntity.ok(dongNames);
    }
}
