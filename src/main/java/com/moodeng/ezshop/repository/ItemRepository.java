package com.moodeng.ezshop.repository;

import com.moodeng.ezshop.entity.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;

public interface ItemRepository extends JpaRepository<Item,Long> {


    // 동적 검색 & 페이지네이션
    // price 필터 추가하기!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    @Query("""
            select i from Item i left join i.category c
            where (i.status = com.moodeng.ezshop.constant.ItemStatus.ACTIVE)
            and (:keyword is null or i.name like %:keyword%)
            and (:categoryName is null or c.name = :categoryName)
    """)
    Page<Item> findBySearchConditions(
            @Param("keyword") String keyword,
            @Param("categotyName") String categoryName,
            Pageable pageable
    );
}
