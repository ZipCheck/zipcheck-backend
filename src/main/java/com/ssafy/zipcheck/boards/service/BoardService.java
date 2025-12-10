package com.ssafy.zipcheck.boards.service;

import com.ssafy.zipcheck.boards.dto.BoardCreateDto;
import com.ssafy.zipcheck.boards.dto.BoardDetailDto;
import com.ssafy.zipcheck.boards.dto.BoardListDto;
import com.ssafy.zipcheck.boards.dto.BoardUpdateDto;
import java.util.List;

public interface BoardService {
    void registerBoard(BoardCreateDto createDto);
    List<BoardListDto> getAllBoards();
    BoardDetailDto getBoardById(int boardId);
    void updateBoard(int boardId, BoardUpdateDto updateDto);
    void deleteBoard(int boardId);
    void likeBoard(int boardId);
}
