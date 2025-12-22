package com.ssafy.zipcheck.boards.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.boards.dto.*;
import com.ssafy.zipcheck.boards.service.BoardService;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // ============================================================
    // 게시글 등록
    // ============================================================
    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BoardCreateDto createDto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        int userId = userDetails.getUser().getUserId();
        createDto.setUserId(userId);
        boardService.registerBoard(createDto);

        return ResponseEntity.status(201)
                .body(ApiResponse.ok("게시글 등록 완료"));
    }

    // ============================================================
    // 게시글 전체 목록
    // ============================================================
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllBoards(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "latest") String order
    ) {
        Integer userId = (userDetails != null)
                ? userDetails.getUser().getUserId()
                : null;

        return ResponseEntity.ok(
                ApiResponse.ok(boardService.getAllBoards(userId, order))
        );
    }

    // ============================================================
    // 게시글 상세
    // ============================================================
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<?>> getBoard(
            @PathVariable int boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Integer userId = (userDetails != null)
                ? userDetails.getUser().getUserId()
                : null;

        BoardDetailDto dto = boardService.getBoardById(boardId, userId);
        if (dto == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.notFound("존재하지 않는 게시글입니다."));
        }

        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // ============================================================
    // 내가 쓴 게시글 조회
    // ============================================================
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<?>> getMyBoards(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        int userId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(
                ApiResponse.ok(boardService.getMyBoards(userId))
        );
    }

    // ============================================================
    // 게시글 수정
    // ============================================================
    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<?>> updateBoard(
            @PathVariable int boardId,
            @RequestBody BoardUpdateDto updateDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        boardService.updateBoard(
                boardId,
                updateDto,
                userDetails.getUser().getUserId()
        );

        return ResponseEntity.ok(ApiResponse.ok("게시글 수정 완료"));
    }

    // ============================================================
    // 게시글 삭제
    // ============================================================
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<?>> deleteBoard(
            @PathVariable int boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
        }

        boardService.deleteBoard(
                boardId,
                userDetails.getUser().getUserId()
        );

        return ResponseEntity.ok(ApiResponse.ok("삭제 완료"));
    }

    // ============================================================
    // 좋아요 토글
    // ============================================================
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<?>> toggleLike(
            @PathVariable int boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        boolean nowLiked = boardService.toggleLike(
                boardId,
                userDetails.getUser().getUserId()
        );

        return ResponseEntity.ok(ApiResponse.ok(nowLiked));
    }
}
