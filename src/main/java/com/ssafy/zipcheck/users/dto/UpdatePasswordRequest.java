package com.ssafy.zipcheck.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private Integer userId;
    private String newPassword;
}
