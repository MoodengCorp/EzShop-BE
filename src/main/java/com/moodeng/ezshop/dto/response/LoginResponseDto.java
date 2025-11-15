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
    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";

    public static LoginResponseDto of(User user, String accessToken) {
        return LoginResponseDto.builder().name(user.getName()).role(user.getRole()).accessToken(accessToken).build();
    }
}
