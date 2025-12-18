package com.ssafy.zipcheck.notice.service;

import com.ssafy.zipcheck.notice.dto.NoticeCreateRequest;
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

    // 전체 / 카테고리별 공지 조회
    @Override
    public List<NoticeResponse> getNotices(String category) {
        return noticeMapper.findAll(category);
    }

    // 공지 상세 조회 (+ 조회수 증가)
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

    // 공지 등록
    @Override
    public void createNotice(int userId, NoticeCreateRequest request) {
        noticeMapper.insert(
                userId,
                request.getTitle(),
                request.getCategory(),
                request.getContent()
        );
    }

    // 공지 수정
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

    // 공지 삭제
    @Override
    public void deleteNotice(int noticeId) {
        int deleted = noticeMapper.delete(noticeId);
        if (deleted == 0) {
            throw new IllegalArgumentException("공지사항 삭제 실패");
        }
    }
}
