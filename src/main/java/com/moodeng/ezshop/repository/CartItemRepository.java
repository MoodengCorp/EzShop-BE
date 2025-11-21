package com.moodeng.ezshop.repository;

import com.moodeng.ezshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CartItemRepository extends JpaRepository<CartItem,Long> {

}
