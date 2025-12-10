package com.ssafy.zipcheck.comments.controller;

import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.service.CommentService;
import com.ssafy.zipcheck.comments.vo.Comments;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody CommentCreateRequest request) {
        try {
            int result = commentService.createComment(request);

            if (result == 0) {
                log.warn("[POST /comments] 댓글 등록 실패");
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.internalError("댓글 등록 중 문제가 발생했습니다."));
            }

            return ResponseEntity.ok(ApiResponse.ok());

        } catch (IllegalArgumentException e) {
            log.warn("[POST /comments] 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("댓글 입력 값이 올바르지 않습니다."));

        } catch (Exception e) {
            log.error("[POST /comments] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("댓글 등록 중 문제가 발생했습니다."));
        }
    }

    /**
     * 특정 게시글의 댓글 전체 조회
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<?>> list(@PathVariable Integer boardId) {
        try {
            List<Comments> comments = commentService.getComments(boardId);
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

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Integer commentId) {
        try {
            int result = commentService.deleteComment(commentId);

            if (result == 0) {
                log.warn("[DELETE /comments/{}] 삭제 대상 없음", commentId);
                return ResponseEntity.status(404)
                        .body(ApiResponse.notFound("존재하지 않는 댓글입니다."));
            }

            return ResponseEntity.ok(ApiResponse.ok());

        } catch (IllegalArgumentException e) {
            log.warn("[DELETE /comments/{}] 잘못된 요청: {}", commentId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.invalid("댓글 삭제 요청이 올바르지 않습니다."));

        } catch (Exception e) {
            log.error("[DELETE /comments/{}] 서버 오류: {}", commentId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("댓글 삭제 중 문제가 발생했습니다."));
        }
    }
}
