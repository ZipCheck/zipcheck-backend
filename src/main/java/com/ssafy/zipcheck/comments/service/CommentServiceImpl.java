package com.ssafy.zipcheck.comments.service;

import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.mapper.CommentMapper;
import com.ssafy.zipcheck.comments.vo.Comments;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    /**
     * 특정 게시글의 댓글 조회
     */
    @Override
    public List<Comments> getComments(Integer boardId) {
        try {
            return commentMapper.findByBoardId(boardId);
        } catch (Exception e) {
            log.error("[SERVICE ERROR] 댓글 조회 실패 boardId={}: {}", boardId, e.getMessage(), e);
            return Collections.emptyList();   // 실패 시 빈 리스트 반환
        }
    }

    /**
     * 댓글 등록
     */
    @Override
    public int createComment(CommentCreateRequest request) {
        try {
            Comments comment = new Comments();
            comment.setBoardId(request.getBoardId());
            comment.setUserId(request.getUserId());
            comment.setContent(request.getContent());

            return commentMapper.insert(comment);
        } catch (Exception e) {
            log.error("[SERVICE ERROR] 댓글 등록 실패: {}", e.getMessage(), e);
            return 0;   // 실패 시 0 반환
        }
    }

    /**
     * 댓글 삭제
     */
    @Override
    public int deleteComment(Integer commentId) {
        try {
            return commentMapper.delete(commentId);
        } catch (Exception e) {
            log.error("[SERVICE ERROR] 댓글 삭제 실패 commentId={}: {}", commentId, e.getMessage(), e);
            return 0;   // 실패 시 0 반환
        }
    }
}
