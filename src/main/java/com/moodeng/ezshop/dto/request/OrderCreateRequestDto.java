package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.entity.Order;
import com.moodeng.ezshop.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequestDto {

    @NotEmpty(message = "주문할 장바구니 상품을 선택해주세요.")
    private List<Long> cartItemIds;

    @NotBlank(message = "수령인 이름을 입력해주세요")
    private String recipientName;

    @NotBlank(message = "수령인 연락처를 입력해주세요")
    private String recipientPhone;

    @NotBlank(message = "배송 주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String addressDetail;

    private String deliveryRequest;

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
