package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.response.CategoryResponseDto;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CommonResponse<CategoryResponseDto>> getAllCategories() {
        CategoryResponseDto responseDto = categoryService.getAllCategories();
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }
}
