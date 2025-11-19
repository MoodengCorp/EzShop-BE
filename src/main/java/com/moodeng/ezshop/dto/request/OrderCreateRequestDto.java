package com.moodeng.ezshop.dto.request;

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


    @NotBlank(message = "배송 주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String addressDetail;

    private String deliveryRequest;
}
