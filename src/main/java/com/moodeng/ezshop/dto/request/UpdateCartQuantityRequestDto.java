package com.moodeng.ezshop.dto.request;

import lombok.Getter;

@Getter
public class UpdateCartQuantityRequestDto {
    private Long cartItemId;
    private Integer quantity;
}
