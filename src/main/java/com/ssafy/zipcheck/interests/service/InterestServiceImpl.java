package com.ssafy.zipcheck.interests.service;

import com.ssafy.zipcheck.interests.dto.InterestCreateRequest;
import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;
import com.ssafy.zipcheck.interests.dto.InterestResponse;
import com.ssafy.zipcheck.interests.mapper.InterestMapper;
import com.ssafy.zipcheck.interests.vo.Interest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestMapper interestMapper;

    @Override
    public Map<String, Object> getInterests(Integer userId, InterestQueryRequest request) {
        request.setUserId(userId); // Set userId from security context
        
        // Calculate offset for pagination
        request.setOffset((request.getSize() * (request.getOffset() > 0 ? request.getOffset() - 1 : 0)));

        List<InterestResponse> interests = interestMapper.findInterestsByUser(request);
        int totalCount = interestMapper.countInterestsByUser(request);

        Map<String, Object> result = new HashMap<>();
        result.put("interests", interests);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    @Transactional
    public void createInterest(Integer userId, InterestCreateRequest request) {
        // Check for existing interest
        Interest existingInterest = interestMapper.findInterestByUserIdAndDealNo(userId, request.getDealNo());
        if (existingInterest != null) {
            throw new IllegalArgumentException("이미 등록된 관심 매물입니다."); // Or a custom exception
        }

        Interest interest = new Interest();
        interest.setUserId(userId);
        interest.setDealNo(request.getDealNo());
        interestMapper.addInterest(interest);
    }

    @Override
    @Transactional
    public void deleteInterest(Integer userId, Integer interestId) {
        // Optional: Add a check to ensure the interestId belongs to the userId
        // For simplicity, the mapper query already includes userId in DELETE WHERE clause.
        interestMapper.deleteInterest(interestId, userId);
    }
}
