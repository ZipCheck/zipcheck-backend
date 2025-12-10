package com.ssafy.zipcheck.comments.service;

import com.ssafy.zipcheck.comments.dto.CommentCreateRequest;
import com.ssafy.zipcheck.comments.vo.Comments;

import java.util.List;

public interface CommentService {

    List<Comments> getComments(Integer boardId);

    int createComment(CommentCreateRequest request);

    int deleteComment(Integer commentId);
}
