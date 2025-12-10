package com.ssafy.zipcheck.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirmRequest {
    private String email;
    private String code;
}
