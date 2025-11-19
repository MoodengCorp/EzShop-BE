package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.request.AddToCartRequestDto;
import com.moodeng.ezshop.dto.request.UpdateCartQuantityRequestDto;
import com.moodeng.ezshop.dto.response.CartItemResponseDto;
import com.moodeng.ezshop.dto.response.CartResponseDto;
import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.entity.Cart;
import com.moodeng.ezshop.entity.CartItem;
import com.moodeng.ezshop.entity.Item;
import com.moodeng.ezshop.exception.BusinessLogicException;
import com.moodeng.ezshop.repository.CartItemRepository;
import com.moodeng.ezshop.repository.CartRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    // 장바구니 조회
    @Transactional
    public CartResponseDto getCart(String userEmail) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new BusinessLogicException(ResponseCode.NOT_FOUND, "장바구니가 존재하지 않습니다."));

        List<CartItemResponseDto> itemDtos = cart.getItems().stream()
                .map(cartItem -> CartItemResponseDto.builder()
                        .cartItemId(cartItem.getId()) // cartItemId 추가함
                        .itemId(cartItem.getItem().getId())
                        .name(cartItem.getItem().getName())
                        .thumbnailUrl(cartItem.getItem().getThumbnailUrl())
                        .price(cartItem.getItem().getPrice())
                        .quantity(cartItem.getQuantity())
                        .build()
                )
                .toList();

        // 전체 아이템 항목 갯수
        int totalCount = cart.getItems().size();
        
        // 방바구니 내 상품 합계금액
        int totalPrice = cart.getItems().stream()
                .mapToInt(cartItem -> cartItem.getItem().getPrice() * cartItem.getQuantity())
                .sum();

        return CartResponseDto.builder()
                .totalCount(totalCount)
                .totalPrice(totalPrice)
                .items(itemDtos)
                .build();
    }

    // 장바구니 담기
    @Transactional
    public void addToCart(String userEmail, AddToCartRequestDto addToCartRequestDto) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new BusinessLogicException(ResponseCode.NOT_FOUND, "장바구니가 존재하지 않습니다."));

        Item item = itemRepository.findById(addToCartRequestDto.getItemId())
                .orElseThrow(() ->
                        new BusinessLogicException(ResponseCode.NOT_FOUND, "상품을 찾을 수 없습니다."));

        CartItem cartItem = cart.getItems().stream()
                .filter(ci -> ci.getItem().getId().equals(item.getId()))
                .findFirst()
                .orElse(null);

        if(cartItem == null) {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .item(item)
                    .quantity(addToCartRequestDto.getQuantity())
                    .build();

            cartItemRepository.save(newCartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequestDto.getQuantity());
        }
    }

    // 장바구니 수량 변경
    @Transactional
    public void updateCartItemQuantity(String userEmail, UpdateCartQuantityRequestDto updateCartQuantityRequestDto) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new BusinessLogicException(ResponseCode.NOT_FOUND, "장바구니가 존재하지 않습니다."));

        CartItem cartItem = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(updateCartQuantityRequestDto.getCartItemId()))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessLogicException(ResponseCode.NOT_FOUND, "장바구니 상품을 찾을 수 없습니다."));

        if (updateCartQuantityRequestDto.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(updateCartQuantityRequestDto.getQuantity());
        }
    }
}
