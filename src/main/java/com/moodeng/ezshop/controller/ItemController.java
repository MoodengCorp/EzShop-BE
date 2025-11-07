package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // 의존성 주입까지 자동으로 해줌
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;


}
