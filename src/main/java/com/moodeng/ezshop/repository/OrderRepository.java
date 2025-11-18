package com.moodeng.ezshop.repository;


import com.moodeng.ezshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
