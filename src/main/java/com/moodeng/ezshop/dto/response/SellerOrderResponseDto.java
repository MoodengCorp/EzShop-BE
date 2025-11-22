package com.moodeng.ezshop.dto.response;

import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.entity.Order;
import com.moodeng.ezshop.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class SellerOrderResponseDto {
    private Long orderId;
    private String orderNumber;
    private String orderDate;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private String buyerName;
    private String buyerPhone;
    private List<OrderItemResponseDto> items;

    public static SellerOrderResponseDto from(Order order, Long sellerId){

        // 주문에서 해당 판매자의 판매상품만 추출
        List<OrderItemResponseDto> sellerItems = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getSeller().getId().equals(sellerId))
                .map(OrderItemResponseDto::fromEntity)
                .toList();

        // 해당 주문에서의 판매자의 총 판매금액
        Integer sellerTotalPrice = sellerItems.stream()
                .mapToInt(orderItem -> orderItem.getPurchasePrice() * orderItem.getQuantity())
                .sum();

        User buyer = order.getUser();
        return SellerOrderResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orderStatus(order.getOrderStatus())
                .totalPrice(sellerTotalPrice)
                .buyerName(buyer.getName())
                .buyerPhone(buyer.getPhone())
                .items(sellerItems)
                .build();
    }

}
