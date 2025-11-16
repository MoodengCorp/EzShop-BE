package com.moodeng.ezshop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    private String address;
    private int itemsCount;
    private int total;
    private List<CartItemResponseDto> items;
}
