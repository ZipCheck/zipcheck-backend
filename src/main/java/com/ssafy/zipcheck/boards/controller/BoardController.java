package com.ssafy.zipcheck.boards.controller;

import com.ssafy.zipcheck.boards.dto.BoardCreateDto;
import com.ssafy.zipcheck.boards.dto.BoardDetailDto;
import com.ssafy.zipcheck.boards.dto.BoardListDto;
import com.ssafy.zipcheck.boards.dto.BoardUpdateDto;
import com.ssafy.zipcheck.boards.service.BoardService;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/zipcheck/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // ============================================================
    // 게시글 등록
    // ============================================================
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerBoard(@RequestBody BoardCreateDto createDto) {
        try {
            boardService.registerBoard(createDto);
            return ResponseEntity.status(201).body(ApiResponse.ok());
        } catch (IllegalArgumentException e) {
            log.warn("[POST /boards] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("게시글 등록 요청이 올바르지 않습니다."));
        } catch (Exception e) {
            log.error("[POST /boards] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 등록 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 전체 게시글 조회
    // ============================================================
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardListDto>>> getAllBoards() {
        try {
            List<BoardListDto> boards = boardService.getAllBoards();
            return ResponseEntity.ok(ApiResponse.ok(boards));
        } catch (Exception e) {
            log.error("[GET /boards] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 조회 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 게시글 상세 조회
    // ============================================================
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailDto>> getBoardById(@PathVariable int boardId) {
        try {
            BoardDetailDto board = boardService.getBoardById(boardId);

            if (board == null) {
                return ResponseEntity.status(404)
                        .body(ApiResponse.notFound("존재하지 않는 게시글입니다."));
            }

            return ResponseEntity.ok(ApiResponse.ok(board));

        } catch (Exception e) {
            log.error("[GET /boards/{}] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 조회 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 게시글 수정
    // ============================================================
    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable int boardId,
            @RequestBody BoardUpdateDto updateDto) {

        try {
            boardService.updateBoard(boardId, updateDto);
            return ResponseEntity.ok(ApiResponse.ok());

        } catch (IllegalArgumentException e) {
            log.warn("[PUT /boards/{}] 잘못된 요청: {}", boardId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("게시글 수정 요청이 올바르지 않습니다."));

        } catch (Exception e) {
            log.error("[PUT /boards/{}] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 수정 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 게시글 삭제
    // ============================================================
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable int boardId) {
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok(ApiResponse.ok());

        } catch (IllegalArgumentException e) {
            log.warn("[DELETE /boards/{}] 잘못된 요청: {}", boardId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("게시글 삭제 요청이 올바르지 않습니다."));

        } catch (Exception e) {
            log.error("[DELETE /boards/{}] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 삭제 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 좋아요
    // ============================================================
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> likeBoard(@PathVariable int boardId) {
        try {
            boardService.likeBoard(boardId);
            return ResponseEntity.ok(ApiResponse.ok());

        } catch (IllegalArgumentException e) {
            log.warn("[POST /boards/{}/like] 잘못된 요청: {}", boardId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("좋아요 요청이 올바르지 않습니다."));

        } catch (Exception e) {
            log.error("[POST /boards/{}/like] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("좋아요 요청 중 문제가 발생했습니다."));
        }
    }
}
