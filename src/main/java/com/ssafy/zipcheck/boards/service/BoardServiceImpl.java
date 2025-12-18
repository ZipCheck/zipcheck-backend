package com.ssafy.zipcheck.boards.service;

import com.ssafy.zipcheck.boards.dto.*;
import com.ssafy.zipcheck.boards.mapper.BoardMapper;
import com.ssafy.zipcheck.boards.mapper.LikeMapper;
import com.ssafy.zipcheck.boards.vo.Boards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final LikeMapper likeMapper;

    @Override
    @Transactional
    public void registerBoard(BoardCreateDto dto) {
        boardMapper.save(dto.toEntity());
    }

    @Override
    public List<BoardListDto> getAllBoards(Integer userId, String order) {
        List<Boards> list = boardMapper.findAll(userId, order);
        return list.stream().map(BoardListDto::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BoardDetailDto getBoardById(int boardId, Integer userId) {
        Boards board = boardMapper.findById(boardId, userId);
        if (board == null) return null;

        boardMapper.incrementHit(boardId);
        return new BoardDetailDto(board);
    }

    @Override
    @Transactional
    public void updateBoard(int boardId, BoardUpdateDto dto, int userId) {
        Boards board = boardMapper.findById(boardId, userId);
        if (board == null)
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");

        if (board.getUserId() != userId)
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");

        board.setTitle(dto.getTitle());
        board.setCategory(dto.getCategory());
        board.setContent(dto.getContent());

        boardMapper.update(board);
    }

    @Override
    @Transactional
    public void deleteBoard(int boardId, int userId) {
        Boards board = boardMapper.findById(boardId, userId);
        if (board == null)
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");

        if (board.getUserId() != userId)
            throw new IllegalArgumentException("본인이 작성한 게시글만 삭제할 수 있습니다.");

        boardMapper.delete(boardId);
    }

    // ============================================================
    // 좋아요 Toggle
    // ============================================================
    @Override
    @Transactional
    public boolean toggleLike(int boardId, int userId) {

        boolean alreadyLiked = likeMapper.existsLike(boardId, userId) > 0;

        if (alreadyLiked) {
            likeMapper.deleteLike(boardId, userId);
            return false;
        } else {
            likeMapper.insertLike(boardId, userId);
            return true;
        }
    }
}
