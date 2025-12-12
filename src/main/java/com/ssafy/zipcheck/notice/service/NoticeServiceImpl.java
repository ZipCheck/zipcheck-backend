package com.ssafy.zipcheck.notice.service;

import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.dto.NoticeListResponse;
import com.ssafy.zipcheck.notice.dto.NoticeResponse;
import com.ssafy.zipcheck.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public void createNotice(int userId, NoticeCreateRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }

        noticeMapper.insert(
                request.getTitle(),
                request.getContent(),
                userId
        );
    }

    @Override
    public List<NoticeListResponse> getNotices() {
        return noticeMapper.findAll();
    }

    @Transactional
    @Override
    public NoticeResponse getNotice(int noticeId) {

        noticeMapper.increaseHit(noticeId);

        NoticeResponse notice = noticeMapper.findById(noticeId);
        if (notice == null) {
            throw new IllegalArgumentException("존재하지 않는 공지사항입니다.");
        }

        return notice;
    }
}
