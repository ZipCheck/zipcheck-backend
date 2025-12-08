package com.ssafy.zipcheck.auth.users.mapper;

import com.ssafy.zipcheck.auth.users.dto.SignupRequest;
import com.ssafy.zipcheck.auth.users.dto.UpdatePasswordRequest;
import com.ssafy.zipcheck.auth.users.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    int existsByEmail(String email);

    int existsByNickname(String nickname);

    int insertUser(@Param("dto") SignupRequest dto, @Param("role") String role);

    int updatePassword(UpdatePasswordRequest dto);

    Optional<User> findByEmail(String email);

    void saveRefreshToken(@Param("email") String email,
                          @Param("token") String token);

    String findRefreshToken(String email);

    void deleteRefreshToken(String email);

}
