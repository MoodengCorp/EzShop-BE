package com.moodeng.ezshop.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequestDto {
    private Long itemId;
    private Integer quantity;
}
