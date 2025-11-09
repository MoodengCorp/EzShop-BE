package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.Role;
import com.moodeng.ezshop.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String address;
    private Role role;

    //Helper Method
    public User toEntity(String encodedPassword) {
        return User.builder().email(email).password(encodedPassword).name(name).phone(phone).address(address).role(role).build();
    }
}
