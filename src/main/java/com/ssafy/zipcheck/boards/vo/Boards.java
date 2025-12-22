package com.ssafy.zipcheck.boards.vo;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boards {

    private int boardId;
    private String title;
    private String category;
    private String content;
    private int hit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int userId;
    private boolean visible;

    private int likeCount;
    private int commentCount;
    private boolean isLiked;

    private String nickname;
    private String profileImageUrl;
}

