package com.moodeng.ezshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
// 필드 하나밖에 없어서 굳이 Dto로 만들 필요는 없지만, 추후 확장가능성과 다른 도메인과의 응답형식 통일성을 위해 dto로 만들었음
public class CategoryResponseDto {
    private final List<String> categories;
    public static CategoryResponseDto from(List<String> categoryNames) {
        return new CategoryResponseDto(categoryNames);
    }
}
