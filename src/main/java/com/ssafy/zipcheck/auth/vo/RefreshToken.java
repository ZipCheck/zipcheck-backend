package com.ssafy.zipcheck.auth.vo;

import lombok.Data;

@Data
public class RefreshToken {
    private String email;
    private String token;
}
