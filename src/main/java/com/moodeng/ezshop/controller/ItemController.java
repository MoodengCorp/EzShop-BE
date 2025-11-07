package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.user.ItemCreateDto;
import com.moodeng.ezshop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // 의존성 주입까지 자동으로 해줌
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<String> createItem(){
        return null;
    }
}
