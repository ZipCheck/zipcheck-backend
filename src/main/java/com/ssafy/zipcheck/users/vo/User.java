package com.ssafy.zipcheck.users.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private int userId;
    private String email;
    private String password;
    private String nickname;
    private String profile_image;
    private boolean status;
    private boolean admin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
