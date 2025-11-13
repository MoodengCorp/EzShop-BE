package com.moodeng.ezshop.dto.response;


import com.moodeng.ezshop.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ItemSimpleResponseDto {
    // 상품 목록조회를 위한 Dto
    private Long itemId;
    private String name;
    private Integer price;
    private String thumbnailUrl; // Full URL

    public static ItemSimpleResponseDto fromEntity(Item item) {
        return ItemSimpleResponseDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .thumbnailUrl(item.getThumbnailUrl())
                .build();
    }
}
