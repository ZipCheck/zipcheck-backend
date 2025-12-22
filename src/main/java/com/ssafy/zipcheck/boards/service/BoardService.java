package com.ssafy.zipcheck.boards.service;

import com.ssafy.zipcheck.boards.dto.*;

import java.util.List;

public interface BoardService {

    void registerBoard(BoardCreateDto dto);

    List<BoardListDto> getAllBoards(Integer userId, String order);

    BoardDetailDto getBoardById(int boardId, Integer userId);

    void updateBoard(int boardId, BoardUpdateDto dto, int userId);

    void deleteBoard(int boardId, int userId);

    boolean toggleLike(int boardId, int userId);

    // ⭐ 추가
    List<BoardListDto> getMyBoards(int userId);
}
