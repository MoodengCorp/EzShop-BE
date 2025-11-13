package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.constant.DeliveryType;
import com.moodeng.ezshop.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDetailResponseDto {

    private Long itemId;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String thumbnailUrl;
    private String detailImageUrl;
    private String origin;
    private DeliveryType deliveryType;
    private String packagingType;
    private String salesUnit;
    private Integer weight;

    public static ItemDetailResponseDto fromEntity(Item item) {
        return ItemDetailResponseDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .thumbnailUrl(item.getThumbnailUrl())
                .detailImageUrl(item.getDetailImageUrl())
                .origin(item.getOrigin())
                .deliveryType(item.getDeliveryType())
                .packagingType(item.getPackagingType())
                .salesUnit(item.getSalesUnit())
                .weight(item.getWeight())
                .build();
    }
}
