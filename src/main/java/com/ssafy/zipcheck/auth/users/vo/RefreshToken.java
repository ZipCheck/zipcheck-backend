package com.ssafy.zipcheck.auth.users.vo;

import lombok.Data;

@Data
public class RefreshToken {
    private String email;
    private String token;
}
