package com.moodeng.ezshop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequestDto {

    private String name;
    private String newPassword;
    private String phone;
    private String address;
}
