package com.ssafy.zipcheck.comments.controller;

import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.service.CommentService;
import com.ssafy.zipcheck.comments.vo.Comments;
import com.ssafy.zipcheck.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

        int result = commentService.createComment(request);

        if (result == 0) {
            // 서비스에서 실패한 경우
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("댓글 등록 중 오류가 발생했습니다."));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok());
    }

    /**
     * 특정 게시글의 댓글 전체 조회
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<?>> list(@PathVariable Integer boardId) {

        List<Comments> comments = commentService.getComments(boardId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(comments));
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Integer commentId) {

        int result = commentService.deleteComment(commentId);

        if (result == 0) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("댓글을 찾을 수 없습니다."));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok());
    }
}
