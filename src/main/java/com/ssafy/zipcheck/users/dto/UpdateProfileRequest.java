package com.ssafy.zipcheck.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String nickname;
    private String profileImage;
}
