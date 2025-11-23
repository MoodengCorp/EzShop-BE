package com.moodeng.ezshop.repository;


import com.moodeng.ezshop.constant.OrderStatus;
import com.moodeng.ezshop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    boolean existsByOrderNumber(String orderNumber);

    // 내 주문목록 찾는 메서드
    @Query("""
                   select distinct o from Order o
                   join fetch o.orderItems oi
                   join fetch oi.item
                   where o.user.email = :email
                   and o.createdAt >= :startDate
                   order by o.createdAt desc
            """)
    List<Order> findMyOrders(@Param("email") String email, @Param("startDate") LocalDateTime startDate);

    // 1. (pagination) 판매자의 주문 Id들만 pagination 포함하여 조회
    // 💡 수정: SELECT DISTINCT에 o.createdAt 추가하여 MySQL 오류 해결.
    @Query("""
                    select distinct o.id, o.createdAt from Order o
                    join o.orderItems oi
                    join o.user u
                    where oi.seller.id = :sellerId
                    and (:orderStatus is null or o.orderStatus = :orderStatus)
                    and (:startDateTime is null or o.createdAt >= :startDateTime)
                    and (:endDateTime is null or o.createdAt <= :endDateTime)
                    and (:itemName is null or oi.item.name like %:itemName%)
                    and (:buyerName is null or u.name like %:buyerName%)
                    order by o.createdAt desc
            """)
    Page<Object[]> findSellerOrderIdsAndCreatedAt(
            @Param("sellerId") Long sellerId,
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("itemName") String itemName,
            @Param("buyerName") String buyerName,
            Pageable pageable);

    // 2. (fetch join) 위에서 찾은 주문 id들을 바탕으로 fetch join
    // User, OrderItem, Item까지 가져옴
    @Query("""
                    select distinct o from Order o
                    join fetch o.orderItems oi
                    join fetch o.user u
                    join fetch oi.item i
                    where o.id in :orderIds
                    order by o.createdAt desc
            """)
    List<Order> findOrdersFetchByIds(@Param("orderIds") List<Long> orderIds);


    @Query("""
                   select o.orderStatus, count(distinct o)
                   from Order o
                   join o.orderItems oi
                   where oi.seller.id = :sellerId
                   group by o.orderStatus
            """)
    List<Object[]> countSellerOrdersByStatus(@Param("sellerId") Long sellerId);
}
