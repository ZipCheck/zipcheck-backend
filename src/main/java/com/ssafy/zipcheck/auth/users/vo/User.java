package com.ssafy.zipcheck.auth.users.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
    private Integer status;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 비밀번호 변경 일자
}
