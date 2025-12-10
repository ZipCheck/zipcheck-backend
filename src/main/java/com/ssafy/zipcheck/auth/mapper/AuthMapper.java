package com.ssafy.zipcheck.auth.mapper;

import com.ssafy.zipcheck.auth.dto.SignupRequest;
import com.ssafy.zipcheck.users.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AuthMapper {

    int existsByEmail(String email);
    int existsByNickname(String nickname);

    int insertUser(@Param("dto") SignupRequest dto,
                   @Param("role") String role);

    Optional<User> findByEmail(String email);

    void saveRefreshToken(@Param("email") String email,
                          @Param("token") String token);

    String findRefreshToken(String email);

    void deleteRefreshToken(String email);
}
