package com.ssafy.zipcheck.comments.mapper;


import com.ssafy.zipcheck.comments.dto.CommentResponse;
import com.ssafy.zipcheck.comments.vo.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentResponse> findByBoardId(@Param("boardId") Integer boardId);

    int insert(Comments comment);

    int delete(@Param("commentId") Integer commentId);

    Integer findOwner(@Param("commentId") Integer commentId);
}
