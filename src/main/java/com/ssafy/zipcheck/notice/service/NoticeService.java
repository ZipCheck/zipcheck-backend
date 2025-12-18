package com.ssafy.zipcheck.notice.service;

import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.dto.NoticeResponse;
import com.ssafy.zipcheck.notice.dto.NoticeUpdateRequest;

import java.util.List;

public interface NoticeService {

    // 전체 / 카테고리별 공지 조회
    List<NoticeResponse> getNotices(String category);

    // 공지 상세
    NoticeResponse getNotice(int noticeId);

    // 공지 등록
    void createNotice(int userId, NoticeCreateRequest request);

    // 공지 수정
    void updateNotice(int noticeId, NoticeUpdateRequest request);

    // 공지 삭제
    void deleteNotice(int noticeId);
}
