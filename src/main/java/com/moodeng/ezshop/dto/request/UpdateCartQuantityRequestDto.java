package com.moodeng.ezshop.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartQuantityRequestDto {
    private Long cartItemId;
    private Integer quantity;
}
