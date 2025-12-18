package com.ssafy.zipcheck.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeUpdateRequest {
    private String title;
    private String category;
    private String content;
}
