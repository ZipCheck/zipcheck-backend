package com.ssafy.zipcheck.interests.service;

import com.ssafy.zipcheck.interests.dto.*;
import com.ssafy.zipcheck.interests.mapper.InterestMapper;
import com.ssafy.zipcheck.interests.vo.Interest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestMapper interestMapper;

    @Override
    public InterestListResponse getInterests(Integer userId, InterestQueryRequest request) {

        int offset = (request.getPage() - 1) * request.getSize();
        request.setOffset(offset);

        var items = interestMapper.findInterestsByUser(userId, request);
        int totalCount = interestMapper.countInterestsByUser(userId, request);

        InterestListResponse response = new InterestListResponse();
        response.setItems(items);
        response.setTotalCount(totalCount);
        response.setPage(request.getPage());
        response.setSize(request.getSize());

        return response;
    }

    @Override
    @Transactional
    public void createInterest(Integer userId, Integer dealNo) {
        Interest existing =
                interestMapper.findByUserIdAndDealNo(userId, dealNo);

        if (existing != null) {
            throw new IllegalArgumentException("이미 등록된 관심 매물입니다.");
        }

        Interest interest = new Interest();
        interest.setUserId(userId);
        interest.setDealNo(dealNo);

        interestMapper.addInterest(interest);
    }

    @Override
    @Transactional
    public void deleteInterest(Integer userId, Integer dealNo) {
        interestMapper.deleteByUserAndDeal(userId, dealNo);
    }
}
