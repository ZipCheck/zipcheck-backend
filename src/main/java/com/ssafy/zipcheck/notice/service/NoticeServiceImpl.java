package com.ssafy.zipcheck.notice.service;

import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
import com.ssafy.zipcheck.notice.dto.NoticeListResponse;
import com.ssafy.zipcheck.notice.dto.NoticeResponse;
import com.ssafy.zipcheck.notice.dto.NoticeUpdateRequest;
import com.ssafy.zipcheck.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    // 공지 목록
    @Override
    public List<NoticeListResponse> getNotices(String category) {
        return noticeMapper.findAll(category);
    }

    // 공지 상세
    @Override
    @Transactional
    public NoticeResponse getNotice(int noticeId) {
        noticeMapper.incrementHit(noticeId);

        NoticeResponse notice = noticeMapper.findById(noticeId);
        if (notice == null) {
            throw new IllegalArgumentException("공지사항이 존재하지 않습니다.");
        }
        return notice;
    }

    @Override
    public void createNotice(int userId, NoticeCreateRequest request) {
        noticeMapper.insert(
                userId,
                request.getTitle(),
                request.getCategory(),
                request.getContent()
        );
    }

    @Override
    public void updateNotice(int noticeId, NoticeUpdateRequest request) {
        int updated = noticeMapper.update(
                noticeId,
                request.getTitle(),
                request.getCategory(),
                request.getContent()
        );

        if (updated == 0) {
            throw new IllegalArgumentException("공지사항 수정 실패");
        }
    }

    @Override
    public void deleteNotice(int noticeId) {
        int deleted = noticeMapper.delete(noticeId);
        if (deleted == 0) {
            throw new IllegalArgumentException("공지사항 삭제 실패");
        }
    }
}
