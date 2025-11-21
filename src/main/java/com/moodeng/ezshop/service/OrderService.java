package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.request.OrderCreateRequestDto;
import com.moodeng.ezshop.dto.response.OrderCreateResponseDto;
import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.entity.CartItem;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.exception.BusinessLogicException;
import com.moodeng.ezshop.repository.CartItemRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import com.moodeng.ezshop.repository.OrderRepository;
import com.moodeng.ezshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public OrderCreateResponseDto createOrder(String email, OrderCreateRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.USER_NOT_FOUND));
        List<CartItem> cartItems = cartItemRepository.findAllById(requestDto.getCartItemIds());
        if (cartItems.isEmpty() || cartItems.size() != requestDto.getCartItemIds().size()) {
            throw new BusinessLogicException(ResponseCode.INVALID_CART_ITEM);
        }


        return null;
    }
}