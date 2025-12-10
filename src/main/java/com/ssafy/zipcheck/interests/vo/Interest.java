package com.ssafy.zipcheck.interests.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Interest {
    private Long interestId;
    private Integer userId;
    private String aptSeq;
    private LocalDateTime createdAt;
}
