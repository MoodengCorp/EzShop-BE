package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.item.request.ItemCreateRequestDto;
import com.moodeng.ezshop.dto.item.request.ItemSearchRequestDto;
import com.moodeng.ezshop.dto.item.response.ItemDetailResponseDto;
import com.moodeng.ezshop.dto.item.response.ItemSearchResponseDto;
import com.moodeng.ezshop.entity.Category;
import com.moodeng.ezshop.entity.Item;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.repository.CategoryRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import com.moodeng.ezshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository  categoryRepository;
    private final ImageStorageService imageStorageService;

    @Transactional
    public void createItem(ItemCreateRequestDto requestDto, MultipartFile thumbnailFile, MultipartFile detailImageFile, Long sellerId) {


        User seller = userRepository.findById(sellerId)
                .orElseThrow(()->new IllegalArgumentException("유효하지 않은 판매자 정보"));
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(()->new IllegalArgumentException("유효하지 않은 카테고리 정보"));

        String thumbnailUrl = imageStorageService.saveFile(thumbnailFile);
        String detailImageUrl = imageStorageService.saveFile(detailImageFile);
        Item item = requestDto.toEntity(seller,category,thumbnailUrl,detailImageUrl);
        itemRepository.save(item);
    }

    // 상품 상세 조회
    public ItemDetailResponseDto getItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return ItemDetailResponseDto.fromEntity(item);
    }

    // 상품 목록 조회
    public ItemSearchResponseDto searchItems(ItemSearchRequestDto requestDto) {
        // 파라미터로부터 필터링조건 정보 추출 필요
        return ItemSearchResponseDto.builder().build();
    }
}
