package com.ssafy.zipcheck.notice.vo;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Notice {

    private Integer noticeId;
    private String title;
    private String content;
    private Integer hit;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
