package com.ssafy.zipcheck.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Integer commentId;
    private String content;
    private String nickname;
    private String createdAt;
}
