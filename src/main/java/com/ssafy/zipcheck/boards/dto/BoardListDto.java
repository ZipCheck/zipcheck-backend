package com.ssafy.zipcheck.boards.dto;

import com.ssafy.zipcheck.boards.vo.Boards;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListDto {

    private int boardId;
    private String title;
    private String category;
    private int hit;
    private int likeCount;
    private int commentCount; // ⭐
    private String nickname;
    private boolean isLiked;
    private LocalDateTime createdAt;

    public BoardListDto(Boards board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.hit = board.getHit();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
        this.nickname = board.getNickname();
        this.isLiked = board.isLiked();
        this.createdAt = board.getCreatedAt();
    }
}

