package com.ssafy.zipcheck.notice.mapper;

import com.ssafy.zipcheck.notice.dto.NoticeListResponse;
import com.ssafy.zipcheck.notice.dto.NoticeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    int insert(
            @Param("title") String title,
            @Param("content") String content,
            @Param("userId") int userId
    );

    List<NoticeListResponse> findAll();

    NoticeResponse findById(@Param("noticeId") int noticeId);

    int increaseHit(@Param("noticeId") int noticeId);
}
