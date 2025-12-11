package com.ssafy.zipcheck.comments.service;

import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.dto.CommentResponse;
import com.ssafy.zipcheck.comments.mapper.CommentMapper;
import com.ssafy.zipcheck.comments.vo.Comments;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponse> getComments(Integer boardId) {
        return commentMapper.findByBoardId(boardId);
    }

    @Override
    public int createComment(CommentCreateRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("댓글 내용이 비어있습니다.");
        }

        Comments comment = new Comments();
        comment.setBoardId(request.getBoardId());
        comment.setUserId(request.getUserId());
        comment.setContent(request.getContent());

        return commentMapper.insert(comment);
    }

    @Override
    public void deleteComment(Integer commentId, int userId) {

        Integer ownerId = commentMapper.findOwner(commentId);
        if (ownerId == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }

        if (!ownerId.equals(userId)) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }

        commentMapper.delete(commentId);
    }
}
