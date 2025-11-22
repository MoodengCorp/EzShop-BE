package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateRequestDto {
    @NotNull(message = "변경할 주문 상태는 필수입니다.")
    private OrderStatus orderStatus;
}
