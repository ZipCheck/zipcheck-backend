package com.ssafy.zipcheck.boards.service;

import com.ssafy.zipcheck.boards.dto.BoardCreateDto;
import com.ssafy.zipcheck.boards.dto.BoardDetailDto;
import com.ssafy.zipcheck.boards.dto.BoardListDto;
import com.ssafy.zipcheck.boards.dto.BoardUpdateDto;
import com.ssafy.zipcheck.boards.mapper.BoardMapper;
import com.ssafy.zipcheck.boards.vo.Boards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    @Override
    @Transactional
    public void registerBoard(BoardCreateDto createDto) {
        boardMapper.save(createDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardListDto> getAllBoards() {
        List<Boards> boards = boardMapper.findAll();
        return boards.stream()
                .map(BoardListDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BoardDetailDto getBoardById(int boardId) {
        boardMapper.incrementHit(boardId);
        Boards board = boardMapper.findById(boardId).orElse(null); // Or throw a custom not found exception
        if (board == null) {
            return null;
        }
        return new BoardDetailDto(board);
    }

    @Override
    @Transactional
    public void updateBoard(int boardId, BoardUpdateDto updateDto) {
        // Find the existing board
        Boards board = boardMapper.findById(boardId).orElse(null); // Or throw
        if (board != null) {
            // Update fields from DTO
            board.setTitle(updateDto.getTitle());
            board.setContent(updateDto.getContent());
            boardMapper.update(board);
        }
        // else throw exception or handle error
    }

    @Override
    @Transactional
    public void deleteBoard(int boardId) {
        boardMapper.delete(boardId);
    }

    @Override
    @Transactional
    public void likeBoard(int boardId) {
        boardMapper.incrementHit(boardId);
    }
}
