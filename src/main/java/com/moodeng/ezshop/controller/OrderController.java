package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.request.OrderCreateRequestDto;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.dto.response.OrderCreateResponseDto;
import com.moodeng.ezshop.entity.Order;
import com.moodeng.ezshop.service.ItemService;
import com.moodeng.ezshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService  orderService;
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderCreateResponseDto>> createOrder(
            @Valid @ModelAttribute OrderCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        String email = userDetails.getUsername();
        OrderCreateResponseDto responseDto = orderService.createOrder(email, requestDto);
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }
}
