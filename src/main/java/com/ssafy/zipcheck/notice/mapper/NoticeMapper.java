package com.ssafy.zipcheck.notice.mapper;

import com.ssafy.zipcheck.notice.dto.NoticeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticeResponse> findAll(
            @Param("category") String category   // 필터용
    );

    NoticeResponse findById(int noticeId);

    int insert(@Param("userId") int userId,
               @Param("title") String title,
               @Param("category") String category,
               @Param("content") String content);

    int update(@Param("noticeId") int noticeId,
               @Param("title") String title,
               @Param("category") String category,
               @Param("content") String content);

    int delete(int noticeId);

    void incrementHit(int noticeId);
}
