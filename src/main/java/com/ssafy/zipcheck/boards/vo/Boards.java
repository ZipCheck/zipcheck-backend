package com.ssafy.zipcheck.boards.vo;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Boards {
    private int boardId;            // PK
    private String title;           // 제목
    private String category;        // 카테고리
    private String content;         // 내용
    private int hit;                // 조회수
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간
    private int userId;             // 작성자 users.user_id
    private boolean visible;        // 공개 여부
    private int likeCount;          // 좋아요 개수
    private String nickname;
    private int commentCount;       // 댓글 개수
    private boolean isLiked;  // 현재 로그인 유저가 좋아요 눌렀는가
}
