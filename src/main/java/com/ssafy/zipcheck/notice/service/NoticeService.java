package com.ssafy.zipcheck.notice.service;

import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.dto.NoticeListResponse;
import com.ssafy.zipcheck.notice.dto.NoticeResponse;
import com.ssafy.zipcheck.notice.dto.NoticeUpdateRequest;

import java.util.List;

public interface NoticeService {

    List<NoticeResponse> getNotices();

    NoticeResponse getNotice(int noticeId);

    void createNotice(int userId, NoticeCreateRequest request);

    void updateNotice(int noticeId, NoticeUpdateRequest request);

    void deleteNotice(int noticeId);
}

