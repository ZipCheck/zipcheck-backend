package com.ssafy.zipcheck.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCreateRequest {
    private String title;
    private String content;
}
