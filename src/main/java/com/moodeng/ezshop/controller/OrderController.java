package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.dto.request.OrderCreateRequestDto;
import com.moodeng.ezshop.dto.request.OrderUpdateRequestDto;
import com.moodeng.ezshop.dto.request.SellerOrderRequestDto;
import com.moodeng.ezshop.dto.response.*;
import com.moodeng.ezshop.service.ItemService;
import com.moodeng.ezshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService  orderService;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderCreateResponseDto>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute OrderCreateRequestDto requestDto
    ) {

        String email = userDetails.getUsername();
        OrderCreateResponseDto responseDto = orderService.createOrder(email, requestDto);
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }

    @GetMapping("/my")
    public ResponseEntity<CommonResponse<List<OrderSimpleResponseDto>>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String period
    ){
        String email = userDetails.getUsername();
        List<OrderSimpleResponseDto> responseDtoList = orderService.getUserOrderList(email, period);
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDtoList));
    }

    @GetMapping("/seller")
    public ResponseEntity<CommonResponse<SellerOrderListResponseDto>> getSellerOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute SellerOrderRequestDto requestDto
    ){
        String email = userDetails.getUsername();
        SellerOrderListResponseDto responseDto = orderService.getSellerOrderList(email, requestDto);
        return  ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }

    @GetMapping("/my/{orderId}")
    public ResponseEntity<CommonResponse<OrderDetailResponseDto>> getOrderDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId
    ){
        String email = userDetails.getUsername();
        OrderDetailResponseDto responseDto = orderService.getOrderDetail(email, orderId);
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<CommonResponse<Void>> updateOrderDetail(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderUpdateRequestDto requestDto
    ){
        orderService.updateOrderStatus(orderId, requestDto.getOrderStatus());
        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    @GetMapping("/seller/orders/status-counts")
    public ResponseEntity<CommonResponse<Map<OrderStatus, Long>>> getSellerOrderStatusCounts(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        String email = userDetails.getUsername();
        Map<OrderStatus, Long> response = orderService.getSellerOrderStatusCounts(email);
        return ResponseEntity.ok(CommonResponse.ofSuccess(response));
    }
}
