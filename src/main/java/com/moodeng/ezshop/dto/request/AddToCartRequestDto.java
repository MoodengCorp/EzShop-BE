package com.moodeng.ezshop.dto.request;

import lombok.Getter;

@Getter
public class AddToCartRequestDto {
    private Long itemId;
    private Integer quantity;
}
