package com.ssafy.zipcheck.users.mapper;

import com.ssafy.zipcheck.users.dto.MyInfoResponse;
import com.ssafy.zipcheck.users.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /* =========================
       조회
       ========================= */
    MyInfoResponse findMyInfo(@Param("userId") Integer userId);

    User findById(@Param("userId") Integer userId);

    User findByEmail(@Param("email") String email);

    String getPassword(@Param("userId") Integer userId);

    /* =========================
       프로필 수정
       ========================= */

    // 닉네임만 수정
    int updateNickname(
            @Param("userId") Integer userId,
            @Param("nickname") String nickname
    );

    // 프로필 이미지 URL만 수정
    int updateProfileImage(
            @Param("userId") Integer userId,
            @Param("profileImageUrl") String profileImageUrl
    );

    /* =========================
       비밀번호
       ========================= */
    int updatePassword(
            @Param("userId") Integer userId,
            @Param("encodedPassword") String encodedPassword
    );

    int updatePasswordByEmail(
            @Param("email") String email,
            @Param("encodedPassword") String encodedPassword
    );

    /* =========================
       알림 설정
       ========================= */
    int updateAlarmSetting(
            @Param("userId") Integer userId,
            @Param("agree") Boolean agree
    );

    int insertAlarmSetting(
            @Param("userId") Integer userId,
            @Param("agree") Boolean agree
    );

    /* =========================
       회원 탈퇴
       ========================= */
    int deleteUser(@Param("userId") Integer userId);
}
