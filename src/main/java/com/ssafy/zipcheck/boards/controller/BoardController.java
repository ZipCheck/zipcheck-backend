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
        try {
            if (userDetails == null) {
                log.warn("[POST /boards] 인증 정보 없음");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            createDto.setUserId(userId);

            boardService.registerBoard(createDto);
            return ResponseEntity.status(201)
                    .body(ApiResponse.ok("게시글 등록 완료"));

        } catch (Exception e) {
            log.error("[POST /boards] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 등록 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 게시글 목록 조회
    // ============================================================
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllBoards(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "latest") String order
    ) {
        try {
            Integer userId = (userDetails != null)
                    ? userDetails.getUser().getUserId()
                    : null;

            List<BoardListDto> list = boardService.getAllBoards(userId, order);
            return ResponseEntity.ok(ApiResponse.ok(list));

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
    public ResponseEntity<ApiResponse<?>> getBoard(
            @PathVariable int boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            Integer userId = (userDetails != null)
                    ? userDetails.getUser().getUserId()
                    : null;

            BoardDetailDto dto = boardService.getBoardById(boardId, userId);

            if (dto == null) {
                return ResponseEntity.status(404)
                        .body(ApiResponse.notFound("존재하지 않는 게시글입니다."));
            }

            return ResponseEntity.ok(ApiResponse.ok(dto));

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
    public ResponseEntity<ApiResponse<?>> updateBoard(
            @PathVariable int boardId,
            @RequestBody BoardUpdateDto updateDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            if (userDetails == null) {
                log.warn("[PATCH /boards/{}] 인증 정보 없음", boardId);
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            boardService.updateBoard(boardId, updateDto, userId);

            return ResponseEntity.ok(ApiResponse.ok("게시글 수정 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[PATCH /boards/{}] 잘못된 요청: {}", boardId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid(e.getMessage()));

        } catch (Exception e) {
            log.error("[PATCH /boards/{}] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 수정 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 게시글 삭제
    // ============================================================
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<?>> deleteBoard(
            @PathVariable int boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            if (userDetails == null) {
                log.warn("[DELETE /boards/{}] 인증 정보 없음", boardId);
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("인증 정보가 없습니다."));
            }

            int userId = userDetails.getUser().getUserId();
            boardService.deleteBoard(boardId, userId);

            return ResponseEntity.ok(ApiResponse.ok("삭제 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[DELETE /boards/{}] 잘못된 요청: {}", boardId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid(e.getMessage()));

        } catch (Exception e) {
            log.error("[DELETE /boards/{}] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("게시글 삭제 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 좋아요 Toggle
    // ============================================================
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<?>> toggleLike(
            @PathVariable int boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            if (userDetails == null) {
                log.warn("[POST /boards/{}/like] 인증 정보 없음", boardId);
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("로그인이 필요합니다."));
            }

            int userId = userDetails.getUser().getUserId();
            boolean nowLiked = boardService.toggleLike(boardId, userId);

            // 메시지는 고정 "OK", data에 nowLiked만 담기
            return ResponseEntity.ok(ApiResponse.ok(nowLiked));

        } catch (Exception e) {
            log.error("[POST /boards/{}/like] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("좋아요 처리 중 문제가 발생했습니다."));
        }
    }

}
