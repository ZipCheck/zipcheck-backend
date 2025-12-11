package com.ssafy.zipcheck.comments.controller;

import com.ssafy.zipcheck.auth.domain.CustomUserDetails;
import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.dto.CommentResponse;
import com.ssafy.zipcheck.comments.service.CommentService;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    // ============================================================
    // 댓글 등록
    // ============================================================
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreateRequest request
    ) {
        try {
            if (userDetails == null) {
                log.warn("[POST /comments] 인증 정보 없음");
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("로그인이 필요합니다."));
            }

            int userId = userDetails.getUser().getUserId();
            request.setUserId(userId);

            commentService.createComment(request);

            log.info("[POST /comments] 댓글 등록 완료 userId={}, boardId={}", userId, request.getBoardId());
            return ResponseEntity.ok(ApiResponse.ok("댓글 등록 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[POST /comments] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.invalid(e.getMessage()));

        } catch (Exception e) {
            log.error("[POST /comments] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("댓글 등록 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 특정 게시글의 댓글 목록 조회
    // ============================================================
    @GetMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<?>> list(@PathVariable Integer boardId) {
        try {
            List<CommentResponse> comments = commentService.getComments(boardId);

            log.info("[GET /comments/board/{}] {}개의 댓글 조회", boardId, comments.size());
            return ResponseEntity.ok(ApiResponse.ok(comments));

        } catch (IllegalArgumentException e) {
            log.warn("[GET /comments/board/{}] 잘못된 요청: {}", boardId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("댓글 조회 요청이 올바르지 않습니다."));

        } catch (Exception e) {
            log.error("[GET /comments/board/{}] 서버 오류: {}", boardId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("댓글 조회 중 문제가 발생했습니다."));
        }
    }

    // ============================================================
    // 댓글 삭제
    // ============================================================
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Integer commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            if (userDetails == null) {
                log.warn("[DELETE /comments/{}] 인증 정보 없음", commentId);
                return ResponseEntity.status(401)
                        .body(ApiResponse.unauthorized("로그인이 필요합니다."));
            }

            int userId = userDetails.getUser().getUserId();
            commentService.deleteComment(commentId, userId);

            log.info("[DELETE /comments/{}] 댓글 삭제 완료 by userId={}", commentId, userId);
            return ResponseEntity.ok(ApiResponse.ok("댓글 삭제 완료"));

        } catch (IllegalArgumentException e) {
            log.warn("[DELETE /comments/{}] 잘못된 요청: {}", commentId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.invalid(e.getMessage()));

        } catch (Exception e) {
            log.error("[DELETE /comments/{}] 서버 오류: {}", commentId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("댓글 삭제 중 문제가 발생했습니다."));
        }
    }
}
