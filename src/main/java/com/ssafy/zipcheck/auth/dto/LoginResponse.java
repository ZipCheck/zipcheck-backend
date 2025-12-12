package com.ssafy.zipcheck.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private int userId;
    private String email;
    private String nickname;
    private String role;
    private String accessToken;
    private String profileImage;
}
