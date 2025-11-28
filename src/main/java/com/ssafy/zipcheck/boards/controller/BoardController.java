package com.ssafy.zipcheck.boards.controller;

import com.ssafy.zipcheck.boards.dto.BoardCreateDto;
import com.ssafy.zipcheck.boards.dto.BoardDetailDto;
import com.ssafy.zipcheck.boards.dto.BoardListDto;
import com.ssafy.zipcheck.boards.dto.BoardUpdateDto;
import com.ssafy.zipcheck.boards.service.BoardService;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zipcheck/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerBoard(@RequestBody BoardCreateDto createDto) {
        boardService.registerBoard(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardListDto>>> getAllBoards() {
        List<BoardListDto> boards = boardService.getAllBoards();
        return ResponseEntity.ok(ApiResponse.ok(boards));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailDto>> getBoardById(@PathVariable int boardId) {
        BoardDetailDto board = boardService.getBoardById(boardId);
        if (board != null) {
            return ResponseEntity.ok(ApiResponse.ok(board));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.notFound("Board not found"));
        }
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(@PathVariable int boardId, @RequestBody BoardUpdateDto updateDto) {
        boardService.updateBoard(boardId, updateDto);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> likeBoard(@PathVariable int boardId) {
        boardService.likeBoard(boardId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}