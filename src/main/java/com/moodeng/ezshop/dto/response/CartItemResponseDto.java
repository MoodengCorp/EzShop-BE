package com.moodeng.ezshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long itemId;
    private String name;
    private String thumbnailUrl;
    private int price;
    private int quantity;
}
