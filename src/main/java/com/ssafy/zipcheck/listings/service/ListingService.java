package com.ssafy.zipcheck.listings.service;

import com.ssafy.zipcheck.listings.dto.ListingQueryRequest;
import com.ssafy.zipcheck.listings.dto.ListingResponse;
import com.ssafy.zipcheck.users.vo.User;

import java.util.List;

public interface ListingService {

    List<ListingResponse> getListings(
            User user,
            ListingQueryRequest request
    );
}
