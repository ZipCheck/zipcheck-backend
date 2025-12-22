package com.ssafy.zipcheck.listings.controller;

import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.listings.dto.ListingQueryRequest;
import com.ssafy.zipcheck.listings.service.ListingService;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getListings(
            @AuthenticationPrincipal(expression = "user") User user,
            @ModelAttribute ListingQueryRequest request
    ) {
        System.out.println("listing user = " + user);
        return ResponseEntity.ok(
                ApiResponse.ok(
                        listingService.getListings(user, request)
                )
        );
    }

}
