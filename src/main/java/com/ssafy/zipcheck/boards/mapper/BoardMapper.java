package com.ssafy.zipcheck.boards.mapper;


import com.ssafy.zipcheck.boards.vo.Boards;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void save(Boards board);
    List<Boards> findAll();
    Optional<Boards> findById(int boardId);
    void update(Boards board);
    void delete(int boardId);
    void incrementHit(int boardId);
    void incrementLike(int boardId);
}
