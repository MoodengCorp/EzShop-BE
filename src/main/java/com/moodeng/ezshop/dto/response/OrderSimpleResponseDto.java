package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.entity.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderSimpleResponseDto {
    private Long orderId;
    private String orderNumber;
    private String orderDate;  // yyyy-MM-dd 형식
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private List<OrderItemResponseDto> items;

    public static OrderSimpleResponseDto fromEntity(Order order) {
        return OrderSimpleResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .items(order.getOrderItems().stream()
                        .map(OrderItemResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
