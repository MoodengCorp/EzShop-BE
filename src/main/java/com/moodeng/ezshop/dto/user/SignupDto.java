package com.moodeng.ezshop.dto.user;

import com.moodeng.ezshop.constant.Role;
import com.moodeng.ezshop.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String address;
    private Role role;

    //Helper Method
    public User toEntity() {
        return User.builder().email(email).password(password).name(name).address(address).role(role).build();
    }
}
