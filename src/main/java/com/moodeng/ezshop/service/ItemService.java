package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.user.ItemCreateDto;
import com.moodeng.ezshop.repository.CategoryRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
//    private final UserRepository userRepository;
    private final CategoryRepository  categoryRepository;

    @Transactional
    public void createItem(ItemCreateDto itemCreateDto) {

    }
}
