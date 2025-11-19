package com.moodeng.ezshop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    private int totalCount;
    private int totalPrice;
    private List<CartItemResponseDto> items;
}
