package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {

    private Long orderId;
    private String orderNumber;
    private String address;
    private String addressDetail;
    private Integer totalPrice;

    public static OrderCreateResponseDto fromEntity(Order order){
        return OrderCreateResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .address(order.getAddress())
                .addressDetail(order.getAddressDetail())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
