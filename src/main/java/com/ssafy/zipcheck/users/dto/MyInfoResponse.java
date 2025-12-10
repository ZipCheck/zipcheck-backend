package com.ssafy.zipcheck.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class MyInfoResponse {
    private Integer userId;
    private String email;
    private String nickname;
    private String profileImage;
    private Boolean alarmAgree;
    private LocalDateTime createdAt;
}
