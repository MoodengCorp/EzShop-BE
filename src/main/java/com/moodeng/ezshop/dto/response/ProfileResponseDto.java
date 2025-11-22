package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.constant.Role;
import com.moodeng.ezshop.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {

    private String email;
    private String name;
    private String phone;
    private String address;
    private String addressDetail;
    private Role role;

    public static ProfileResponseDto from(User user) {
        return ProfileResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .role(user.getRole())
                .build();
    }
}
