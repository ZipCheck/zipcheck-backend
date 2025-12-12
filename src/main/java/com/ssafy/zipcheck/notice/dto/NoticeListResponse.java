package com.ssafy.zipcheck.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeListResponse {
    private Integer noticeId;
    private String title;
    private String nickname;
    private Integer hit;
    private LocalDateTime createdAt;
}
