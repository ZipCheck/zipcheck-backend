package com.ssafy.zipcheck.boards.dto;

import com.ssafy.zipcheck.boards.vo.Boards;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class BoardDetailDto {
    private final int boardId;
    private final String title;
    private final String content;
    private final int hit;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String nickname;
    private final int likeCount;
    private final boolean isLiked;

    public BoardDetailDto(Boards board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.hit = board.getHit();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.nickname = board.getNickname();
        this.likeCount = board.getLikeCount();
        this.isLiked = board.isLiked();
    }
}
