package com.moodeng.ezshop.repository;

import com.moodeng.ezshop.entity.CartItem;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository {
    void save(CartItem newCartItem);

    void delete(CartItem cartItem);
}
