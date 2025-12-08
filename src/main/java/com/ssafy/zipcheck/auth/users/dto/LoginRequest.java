package com.ssafy.zipcheck.auth.users.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;
    private String password;
}
