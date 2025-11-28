package com.ssafy.zipcheck.comments.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    private Integer boardId;
    private Integer userId;
    private String content;
}
