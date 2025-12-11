package com.ssafy.zipcheck.boards.dto;

import com.ssafy.zipcheck.boards.vo.Boards;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListDto {
    private final int boardId;
    private final String title;
    private final int hit;
    private final LocalDateTime createdAt;
    private final String nickname;
    private final int likeCount;
    private final boolean isLiked;

    public BoardListDto(Boards board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.hit = board.getHit();
        this.createdAt = board.getCreatedAt();
        this.nickname = board.getNickname();
        this.likeCount = board.getLikeCount();
        this.isLiked = board.isLiked();
    }
}
