package com.ssafy.zipcheck.interests;

import com.ssafy.zipcheck.interests.dto.InterestCreateRequest;
import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;
import com.ssafy.zipcheck.interests.mapper.InterestMapper;
import com.ssafy.zipcheck.interests.service.InterestServiceImpl;
import com.ssafy.zipcheck.interests.vo.Interest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestServiceTest {

    @Mock
    private InterestMapper interestMapper;

    @InjectMocks
    private InterestServiceImpl interestService;

    private Integer testUserId;
    private Integer testDealNo;
    private Integer testInterestId;

    @BeforeEach
    void setUp() {
        testUserId = 1;
        testDealNo = 123;
        testInterestId = 100;
    }

    @Test
    @DisplayName("관심 매물 목록 조회 성공")
    void getInterests_success() {
        // Given
        InterestQueryRequest queryRequest = new InterestQueryRequest();
        queryRequest.setOffset(1); // Page 1
        queryRequest.setSize(3); // 3 items per page

        when(interestMapper.findInterestsByUser(any(InterestQueryRequest.class))).thenReturn(Collections.emptyList());
        when(interestMapper.countInterestsByUser(any(InterestQueryRequest.class))).thenReturn(0);

        // When
        Map<String, Object> result = interestService.getInterests(testUserId, queryRequest);

        // Then
        assertNotNull(result);
        assertTrue(result.containsKey("interests"));
        assertTrue(result.containsKey("totalCount"));
        assertEquals(0, ((List<?>) result.get("interests")).size());
        assertEquals(0, result.get("totalCount"));

        verify(interestMapper, times(1)).findInterestsByUser(any(InterestQueryRequest.class));
        verify(interestMapper, times(1)).countInterestsByUser(any(InterestQueryRequest.class));
    }

    @Test
    @DisplayName("관심 매물 등록 성공")
    void createInterest_success() {
        // Given
        InterestCreateRequest createRequest = new InterestCreateRequest();
        createRequest.setDealNo(testDealNo);

        when(interestMapper.findInterestByUserIdAndDealNo(anyInt(), anyInt())).thenReturn(null);
        doNothing().when(interestMapper).addInterest(any(Interest.class));

        // When
        interestService.createInterest(testUserId, createRequest);

        // Then
        verify(interestMapper, times(1)).findInterestByUserIdAndDealNo(testUserId, testDealNo);
        verify(interestMapper, times(1)).addInterest(any(Interest.class));
    }

    @Test
    @DisplayName("관심 매물 등록 실패 - 이미 존재하는 경우")
    void createInterest_fail_alreadyExists() {
        // Given
        InterestCreateRequest createRequest = new InterestCreateRequest();
        createRequest.setDealNo(testDealNo);

        when(interestMapper.findInterestByUserIdAndDealNo(anyInt(), anyInt())).thenReturn(new Interest()); // Simulate existing
        
        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            interestService.createInterest(testUserId, createRequest);
        });
        assertEquals("이미 등록된 관심 매물입니다.", thrown.getMessage());

        verify(interestMapper, times(1)).findInterestByUserIdAndDealNo(testUserId, testDealNo);
        verify(interestMapper, never()).addInterest(any(Interest.class));
    }

    @Test
    @DisplayName("관심 매물 삭제 성공")
    void deleteInterest_success() {
        // Given
        doNothing().when(interestMapper).deleteInterest(anyInt(), anyInt());

        // When
        interestService.deleteInterest(testUserId, testInterestId);

        // Then
        verify(interestMapper, times(1)).deleteInterest(testInterestId, testUserId);
    }
}
