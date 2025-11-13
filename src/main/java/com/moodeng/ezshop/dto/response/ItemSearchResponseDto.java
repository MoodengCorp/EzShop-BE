package com.moodeng.ezshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ItemSearchResponseDto {
    private List<ItemSimpleResponseDto> items;
    private PaginationDto pagination;
}
