package com.ssafy.zipcheck.boards.dto;

import com.ssafy.zipcheck.boards.vo.Boards;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateDto {
    private String title;
    private String content;
    private int userId; // Assuming the user ID is sent from the client

    public Boards toEntity() {
        return Boards.builder()
                .title(this.title)
                .content(this.content)
                .userId(this.userId)
                .build();
    }
}
