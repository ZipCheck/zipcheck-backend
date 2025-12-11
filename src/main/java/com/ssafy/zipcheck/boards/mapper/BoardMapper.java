package com.ssafy.zipcheck.boards.mapper;

import com.ssafy.zipcheck.boards.vo.Boards;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    void save(Boards board);
    List<Boards> findAll(@Param("userId") Integer userId, @Param("order") String order);
    Boards findById(@Param("boardId") int boardId, @Param("userId") Integer userId);
    void update(Boards board);
    void delete(int boardId);
    void incrementHit(int boardId);
}
