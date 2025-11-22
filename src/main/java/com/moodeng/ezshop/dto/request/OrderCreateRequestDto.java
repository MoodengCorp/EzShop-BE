package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.entity.Order;
import com.moodeng.ezshop.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequestDto {

    @NotNull(message = "장바구니 ID를 입력해주세요.")
    private Long cartId;

    @Min(value = 0, message = "총 금액은 0원 이상이어야 합니다.")
    private int totalPrice;

    @NotEmpty(message = "주문할 장바구니 상품을 선택해주세요.")
    private List<OrderItemInfoRequestDto> orderItemInfo;

    @NotBlank(message = "수령인 이름을 입력해주세요")
    private String recipientName;

    @NotBlank(message = "수령인 연락처를 입력해주세요")
    private String recipientPhone;

    @NotBlank(message = "배송 주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String addressDetail;

    private String deliveryRequest;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfoRequestDto {
        @NotNull(message = "장바구니 상품 ID는 필수입니다.")
        private Long cartItemId;

        @NotNull(message = "상품 ID는 필수입니다.")
        private Long itemId;

        @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
        private int price;

        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private int quantity;
    }

    public Order toEntity(User user, String orderNumber){
        return Order.builder()
                .user(user)
                .orderNumber(orderNumber)
                .orderStatus(OrderStatus.PENDING)
                .recipientName(recipientName)
                .recipientPhone(recipientPhone)
                .address(address)
                .addressDetail(addressDetail)
                .deliveryRequest(deliveryRequest)
                .totalPrice(0)
                .build();
    }

}
