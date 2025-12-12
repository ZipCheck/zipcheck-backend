package com.ssafy.zipcheck.interests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.interests.controller.InterestController;
import com.ssafy.zipcheck.interests.dto.InterestCreateRequest;
import com.ssafy.zipcheck.interests.dto.InterestQueryRequest;
import com.ssafy.zipcheck.interests.service.InterestService;
import com.ssafy.zipcheck.users.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InterestController.class)
class InterestControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockBean

        private InterestService interestService;

        @MockBean

        private com.ssafy.zipcheck.auth.jwt.JwtUtil jwtUtil;

        @MockBean

        private com.ssafy.zipcheck.users.mapper.UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setNickname("testUser");
        mockUser.setRole("ROLE_USER");
        mockUserDetails = new CustomUserDetails(mockUser);
    }



    @Test
    @DisplayName("GET /api/interests - 관심 매물 목록 조회 성공")
    void getInterests_success() throws Exception {
        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("interests", Collections.emptyList());
        serviceResult.put("totalCount", 0);

        when(interestService.getInterests(anyInt(), any(InterestQueryRequest.class)))
                .thenReturn(serviceResult);

        mockMvc.perform(get("/api/interests")
                        .with(user(mockUserDetails)) // Pass mockUser as principal
                        .param("offset", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.interests").isArray())
                .andExpect(jsonPath("$.data.totalCount").value(0));

        verify(interestService, times(1)).getInterests(anyInt(), any(InterestQueryRequest.class));
    }

    @Test
    @DisplayName("POST /api/interests - 관심 매물 등록 성공")
    void createInterest_success() throws Exception {
        InterestCreateRequest createRequest = new InterestCreateRequest();
        createRequest.setDealNo(123);

        doNothing().when(interestService).createInterest(anyInt(), any(InterestCreateRequest.class));

        mockMvc.perform(post("/api/interests")
                        .with(user(mockUserDetails))
                        .with(csrf()) // Required for POST in Spring Security
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("관심 매물 등록 성공"));

        verify(interestService, times(1)).createInterest(anyInt(), any(InterestCreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/interests - 관심 매물 등록 실패 (중복)")
    void createInterest_fail_duplicate() throws Exception {
        InterestCreateRequest createRequest = new InterestCreateRequest();
        createRequest.setDealNo(123);

        doThrow(new IllegalArgumentException("이미 등록된 관심 매물입니다."))
                .when(interestService).createInterest(anyInt(), any(InterestCreateRequest.class));

        mockMvc.perform(post("/api/interests")
                        .with(user(mockUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isConflict()) // 409 Conflict
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("이미 등록된 관심 매물입니다."));

        verify(interestService, times(1)).createInterest(anyInt(), any(InterestCreateRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/interests/{interestId} - 관심 매물 삭제 성공")
    void deleteInterest_success() throws Exception {
        Integer interestIdToDelete = 1;

        doNothing().when(interestService).deleteInterest(anyInt(), anyInt());

        mockMvc.perform(delete("/api/interests/{interestId}", interestIdToDelete)
                        .with(user(mockUserDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("관심 매물 삭제 성공"));

        verify(interestService, times(1)).deleteInterest(anyInt(), anyInt());
    }
}
