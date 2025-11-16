package com.moodeng.ezshop.service;


import com.moodeng.ezshop.dto.response.CategoryResponseDto;
import com.moodeng.ezshop.entity.Category;
import com.moodeng.ezshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto getAllCategories(){

        List<Category> categories = categoryRepository.findAll();
        List<String> categoryNames =categories.stream()
                .map(Category::getName)
                .toList();
        return CategoryResponseDto.from(categoryNames);
    }

}
