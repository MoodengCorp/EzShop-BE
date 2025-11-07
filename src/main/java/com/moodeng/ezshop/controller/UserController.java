package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.user.SignupDto;
import com.moodeng.ezshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto) {

        userService.signup(signupDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
