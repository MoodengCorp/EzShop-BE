package com.moodeng.ezshop.repository;

import com.moodeng.ezshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
