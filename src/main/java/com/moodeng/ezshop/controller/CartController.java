package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

//    @PostMapping
//
//    @GetMapping
//
//    @PatchMapping
}
