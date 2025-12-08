package com.ssafy.zipcheck.auth.users.mapper;

import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    int existsByEmail(String email);

    int existsByNickname(String nickname);

    int insertUser(SignupRequest dto);

    int updatePassword(UpdatePasswordRequest dto);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer userId);
}
