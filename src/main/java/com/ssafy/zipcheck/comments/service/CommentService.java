package com.ssafy.zipcheck.comments.service;

import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getComments(Integer boardId);

    int createComment(CommentCreateRequest request);

    void deleteComment(Integer commentId, int userId);
}
