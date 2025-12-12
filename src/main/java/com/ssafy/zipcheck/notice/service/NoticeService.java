package com.ssafy.zipcheck.notice.service;

import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.dto.NoticeListResponse;
import com.ssafy.zipcheck.notice.dto.NoticeResponse;

import java.util.List;

public interface NoticeService {

    void createNotice(int userId, NoticeCreateRequest request);

    List<NoticeListResponse> getNotices();

    NoticeResponse getNotice(int noticeId);
}
