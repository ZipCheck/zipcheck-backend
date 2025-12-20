package com.ssafy.zipcheck.users.mapper;

import com.ssafy.zipcheck.users.dto.MyInfoResponse;
import com.ssafy.zipcheck.users.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    MyInfoResponse findMyInfo(@Param("userId") Integer userId);

    User findById(@Param("userId") Integer userId);

    User findByEmail(@Param("email") String email);

    String getPassword(@Param("userId") Integer userId);

    int updateProfile(
            @Param("userId") Integer userId,
            @Param("nickname") String nickname,
            @Param("profileImageUrl") String profileImageUrl
    );

    int updatePassword(
            @Param("userId") Integer userId,
            @Param("encodedPassword") String encodedPassword
    );

    int updatePasswordByEmail(
            @Param("email") String email,
            @Param("encodedPassword") String encodedPassword
    );

    int updateAlarmSetting(
            @Param("userId") Integer userId,
            @Param("agree") Boolean agree
    );

    int insertAlarmSetting(
            @Param("userId") Integer userId,
            @Param("agree") Boolean agree
    );

    int deleteUser(@Param("userId") Integer userId);
}
