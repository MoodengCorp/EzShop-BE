package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponseDto {
    private Long itemId;
    private String name;
    private Integer quantity;
    private Integer purchasePrice;
    private String thumbnailUrl;

    public static OrderItemResponseDto fromEntity(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .itemId(orderItem.getItem().getId())
                .name(orderItem.getItem().getName())
                .quantity(orderItem.getQuantity())
                .purchasePrice(orderItem.getPurchasePrice())
                .thumbnailUrl(orderItem.getItem().getThumbnailUrl())
                .build();
    }
}
