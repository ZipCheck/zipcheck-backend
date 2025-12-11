package com.ssafy.zipcheck.boards.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {

    int existsLike(@Param("boardId") int boardId, @Param("userId") int userId);

    int insertLike(@Param("boardId") int boardId, @Param("userId") int userId);

    int deleteLike(@Param("boardId") int boardId, @Param("userId") int userId);
}
