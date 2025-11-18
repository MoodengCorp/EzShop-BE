package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.request.OrderCreateRequestDto;
import com.moodeng.ezshop.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(OrderCreateRequestDto requestDto) {


    }
}
