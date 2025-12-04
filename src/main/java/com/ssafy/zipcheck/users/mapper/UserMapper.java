package com.ssafy.zipcheck.users.mapper;

import com.ssafy.zipcheck.users.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByEmail(String email);
    Optional<User> findById(int userId);
    void save(User user);
    void deleteById(int userId);
}
