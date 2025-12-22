package com.ssafy.zipcheck.listings.service;

import com.ssafy.zipcheck.listings.dto.ListingQueryRequest;
import com.ssafy.zipcheck.listings.dto.ListingResponse;
import com.ssafy.zipcheck.listings.mapper.ListingMapper;
import com.ssafy.zipcheck.users.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingMapper listingMapper;

    @Override
    public List<ListingResponse> getListings(User user, ListingQueryRequest request) {

        int offset = (request.getPage() - 1) * request.getSize();
        request.setOffset(offset);

        Integer userId = (user != null) ? user.getUserId() : null;

        return listingMapper.findListings(userId, request);
    }
}
