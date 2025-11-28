package com.ssafy.zipcheck.comments.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comments {

    private Integer commentId;
    private Integer boardId;
    private Integer userId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean visible;
}
