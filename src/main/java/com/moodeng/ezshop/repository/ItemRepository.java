package com.moodeng.ezshop.repository;

import com.moodeng.ezshop.constant.ItemStatus;
import com.moodeng.ezshop.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    // 동적 검색 & 페이지네이션
    // pagination으로 인해 자동으로 정렬까지 됨
    @Query("""
            select i from Item i left join i.category c
            where (:status is null or i.status = :status)
            and (:keyword is null or i.name like %:keyword%)
            and (:categoryName is null or c.name = :categoryName)
            and (:minPrice is null or i.price >= :minPrice)
            and (:maxPrice is null or i.price <= :maxPrice)
            and (:sellerEmail is null or i.user.email = :sellerEmail)
    """)
    Page<Item> findBySearchConditions(
            @Param("status") ItemStatus status,
            @Param("keyword") String keyword,
            @Param("categoryName") String categoryName,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("sellerEmail") String sellerEmail,
            Pageable pageable
    );
}
