package com.moodeng.ezshop.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteCartItemsRequestDto {

    @NotEmpty(message = "삭제할 장바구니 아이템 ID 목록은 비어 있을 수 없습니다.")
    private List<Long> cartItemIds;
}
