package com.ssafy.zipcheck.interests.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Interest {
    private Integer interestId;
    private Integer userId;
    private Integer dealNo;
    private LocalDateTime createdAt;
}
