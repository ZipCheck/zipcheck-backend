package com.ssafy.zipcheck.users.mapper;

import com.ssafy.zipcheck.users.dto.MyInfoResponse;
import com.ssafy.zipcheck.users.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    MyInfoResponse findMyInfo(Integer userId);

    User findById(Integer userId);

    String getPassword(Integer userId);

    int updateProfile(@Param("userId") Integer userId,
                      @Param("nickname") String nickname,
                      @Param("profileImage") String profileImage);

    int updatePassword(@Param("userId") Integer userId,
                       @Param("encodedPassword") String encodedPassword);

    int updateAlarmSetting(@Param("userId") Integer userId,
                           @Param("agree") Boolean agree);

    int insertAlarmSetting(@Param("userId") Integer userId,
                           @Param("agree") Boolean agree);

    int deleteUser(Integer userId);

    int updatePasswordByEmail(@Param("email") String email,
                              @Param("encodedPassword") String encodedPassword);

    User findByEmail(String email);

}


