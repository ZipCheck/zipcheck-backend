package com.ssafy.zipcheck.users.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String nickname;
    private String profileImageUrl;
    private Integer status;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
