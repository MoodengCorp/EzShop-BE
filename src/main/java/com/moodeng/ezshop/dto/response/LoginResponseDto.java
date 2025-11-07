package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.constant.Role;
import com.moodeng.ezshop.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponseDto {
    private String name;
    private Role role;

    public static LoginResponseDto fromEntity(User user) {
        return LoginResponseDto.builder().name(user.getName()).role(user.getRole()).build();
    }
}
