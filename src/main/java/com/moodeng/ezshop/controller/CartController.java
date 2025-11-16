package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.request.AddToCartRequestDto;
import com.moodeng.ezshop.dto.request.UpdateCartQuantityRequestDto;
import com.moodeng.ezshop.dto.response.CartResponseDto;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CommonResponse<CartResponseDto>> getCart(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        String userEmail = userDetails.getUsername();
        CartResponseDto cartResponseDto = cartService.getCart(userEmail);
        return ResponseEntity.ok(CommonResponse.ofSuccess(cartResponseDto));
    }

    // 장바구니 담기
    @PostMapping
    public ResponseEntity<CommonResponse<Void>> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddToCartRequestDto addToCartRequestDto
            ) {
        String userEmail = userDetails.getUsername();
        cartService.addToCart(userEmail, addToCartRequestDto);
        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    // 장바구니 수량 수정
    @PatchMapping
    public ResponseEntity<CommonResponse<Void>> updateCartQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateCartQuantityRequestDto updateCartQuantityRequestDto
            ) {
        String userEmail = userDetails.getUsername();
        cartService.updateCartItemQuantity(userEmail, updateCartQuantityRequestDto);
        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }
}
