package com.moodeng.ezshop.service;

import com.moodeng.ezshop.constant.ItemStatus;
import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.dto.request.OrderCreateRequestDto;
import com.moodeng.ezshop.dto.request.SellerOrderRequestDto;
import com.moodeng.ezshop.dto.response.*;
import com.moodeng.ezshop.entity.*;
import com.moodeng.ezshop.exception.BusinessLogicException;
import com.moodeng.ezshop.repository.CartItemRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import com.moodeng.ezshop.repository.OrderRepository;
import com.moodeng.ezshop.repository.UserRepository;
import com.moodeng.ezshop.util.OrderNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderCreateResponseDto createOrder(String email, OrderCreateRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.USER_NOT_FOUND));

        // 장바구니에 담긴 상품들 중 주문할 상품들 일괄 조회 (쿼리 1번만 하기위해서)
        List<CartItem> cartItems = cartItemRepository.findAllById(requestDto.getCartItemIds());

        if (cartItems.isEmpty() || cartItems.size() != requestDto.getCartItemIds().size()) {
            throw new BusinessLogicException(ResponseCode.INVALID_CART_ITEM);
        }

        // 주문번호 생성
        String orderNumber = generateUniqueOrderNumber();

        // 주문 엔티티 생성 (totalPrice는 0인 상태)
        Order order = requestDto.toEntity(user, orderNumber);

        int totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
                throw new BusinessLogicException(ResponseCode.FORBIDDEN);
            }
            Item item = cartItem.getItem();
            if (item.getStatus() != ItemStatus.ACTIVE) {
                throw new BusinessLogicException(ResponseCode.ITEM_NOT_FOR_SALE);
            }
            if (item.getStockQuantity() < cartItem.getQuantity()){
                throw new BusinessLogicException(ResponseCode.OUT_OF_STOCK);
            }
            item.setStockQuantity(item.getStockQuantity() - cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .order(order)
                    .seller(item.getUser())
                    .quantity(cartItem.getQuantity())
                    .purchasePrice(item.getPrice())
                    .build();
            // 연관관계 편의 메서드로 orderItem에도 Order필드 추가
            order.addOrderItem(orderItem);
            totalPrice += item.getPrice() * cartItem.getQuantity();
        }

        // 총액 설정 및 order 저장
        order.setTotalPrice(totalPrice);

        // CascadeType.ALL로 설정했기 때문에 orderItem들도 자동으로 DB에 저장
        orderRepository.save(order);

        // 주문된 상품 대상으로 장바구니 정리
        cartItemRepository.deleteAll(cartItems);

        return OrderCreateResponseDto.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public List<OrderSimpleResponseDto> getUserOrderList(String email, String period) {
        LocalDateTime startDate = calculateStartDate(period);
        List<Order> orders = orderRepository.findMyOrders(email, startDate);

        return orders.stream()
                .map(OrderSimpleResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SellerOrderListResponseDto getSellerOrderList(String email, SellerOrderRequestDto requestDto) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.SELLER_NOT_FOUND));
        Page<Order> orderPage = orderRepository.findSellerOrders(
                seller.getId(),
                requestDto.getOrderStatus(),
                requestDto.getStartDateTime(),
                requestDto.getEndDateTime(),
                requestDto.getItemName(),
                requestDto.getBuyerName(),
                requestDto.toPageable()
        );

        List<SellerOrderResponseDto> sellerOrderList = orderPage.getContent().stream()
                .map(order -> SellerOrderResponseDto.from(order, seller.getId()))
                .toList();
        return SellerOrderListResponseDto.of(sellerOrderList, orderPage);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.USER_NOT_FOUND));

        //주문 하나당 item이 많지는 않을 것 같아서 성능향상을 위한 fetch join을 쓰지는 않음
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.NOT_FOUND));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BusinessLogicException(ResponseCode.FORBIDDEN);
        }
        return OrderDetailResponseDto.fromEntity(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.NOT_FOUND));

        order.setOrderStatus(orderStatus);
    }

    @Transactional(readOnly = true)
    public Map<OrderStatus, Long> getSellerOrderStatusCounts(String email){
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.SELLER_NOT_FOUND));
        List<Object[]> results = orderRepository.countSellerOrdersByStatus(seller.getId());

        Map<OrderStatus, Long> statusCounts = new EnumMap<>(OrderStatus.class);
        for (OrderStatus value : OrderStatus.values()) {
            statusCounts.put(value, 0L);
        }

        for (Object[] result : results) {
            statusCounts.put((OrderStatus) result[0], (Long) result[1]);
        }
        return statusCounts;
    }


    // period를 String에서 LocalDateTime으로 바꿔주는 헬퍼메서드
    private LocalDateTime calculateStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        if ("3개월".equals(period)){
            return now.minusMonths(3);
        } else if ("6개월".equals(period)) {
            return now.minusMonths(6);
        } else if ("1년".equals(period)) {
            return now.minusYears(1);
        } else{
            // 조회기간 정보가 없는 경우에 모든 기간 조회 가능
            return LocalDateTime.of(1900,1,1,0,0);
        }
    }

    // 유일한 주문번호를 만들어주는 헬퍼메서드
    private String generateUniqueOrderNumber(){
        String orderNumber;
        // ordernumber가 일치하는 경우에 대한 예외처리
        do {
            orderNumber = OrderNumberGenerator.generateOrderNumber();
        } while (orderRepository.existsByOrderNumber(orderNumber));
        return orderNumber;
    }
}