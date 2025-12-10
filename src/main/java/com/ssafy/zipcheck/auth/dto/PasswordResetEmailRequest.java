package com.ssafy.zipcheck.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetEmailRequest {
    private String email;
}
