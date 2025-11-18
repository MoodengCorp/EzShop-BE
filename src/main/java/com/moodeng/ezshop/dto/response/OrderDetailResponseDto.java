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
public class OrderDetailResponseDto {
    private Long orderId;
    private String orderNumber;
    private String orderDate;  // yyyy-MM-dd 형식
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private List<OrderItemResponseDto> items;

    // ✅ 배송 정보
    private DeliveryInfoDto deliveryInfo;
    private String deliveryRequest;

    public static OrderDetailResponseDto fromEntity(Order order) {
        return OrderDetailResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .items(order.getOrderItems().stream()
                        .map(OrderItemResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .deliveryInfo(DeliveryInfoDto.from(order))
                .deliveryRequest(order.getDeliveryRequest())
                .build();
    }

    // ✅ 배송 정보 DTO (내부 클래스)
    @Getter
    @Builder
    public static class DeliveryInfoDto {
        private String recipientName;
        private String recipientPhone;
        private String address;
        private String addressDetail;

        public static DeliveryInfoDto from(Order order) {
            return DeliveryInfoDto.builder()
                    .recipientName(order.getRecipientName())
                    .recipientPhone(order.getRecipientPhone())
                    .address(order.getAddress())
                    .addressDetail(order.getAddressDetail())
                    .build();
        }
    }
}
