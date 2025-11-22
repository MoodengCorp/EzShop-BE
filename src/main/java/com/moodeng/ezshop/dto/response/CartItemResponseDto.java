package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.constant.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long cartItemId; // cartItemId 추가함
    private Long itemId;
    private DeliveryType deliveryType; // deliveryType 추가
    private String name;
    private String thumbnailUrl;
    private int price;
    private int quantity;
}
