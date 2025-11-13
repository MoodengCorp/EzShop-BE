package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.request.ItemCreateRequestDto;
import com.moodeng.ezshop.dto.request.ItemSearchRequestDto;
import com.moodeng.ezshop.dto.response.ItemDetailResponseDto;
import com.moodeng.ezshop.dto.response.ItemSearchResponseDto;
import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.entity.Category;
import com.moodeng.ezshop.entity.Item;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.exception.BusinessLogicException;
import com.moodeng.ezshop.repository.CategoryRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import com.moodeng.ezshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository  categoryRepository;
    private final ImageStorageService imageStorageService;

    @Transactional
    public void createItem(ItemCreateRequestDto requestDto, MultipartFile thumbnailFile, MultipartFile detailImageFile, String email) {

        User seller = userRepository.findByEmail(email)
                        .orElseThrow(()->new BusinessLogicException(ResponseCode.SELLER_NOT_FOUND));

        // 인가 관련 로직은 SecurityConfig에서 다루기 때문에 seller의 Role은 여기에서 굳이 확인하지 않음
        // 카테고리 이름으로 변경
        Category category = categoryRepository.findByName(requestDto.getCategoryName())
                        .orElseThrow(()->new BusinessLogicException(ResponseCode.CATEGORY_NOT_FOUND));

        String thumbnailUrl = imageStorageService.saveFile(thumbnailFile);
        String detailImageUrl = imageStorageService.saveFile(detailImageFile);
        Item item = requestDto.toEntity(seller,category,thumbnailUrl,detailImageUrl);
        itemRepository.save(item);
    }

    // 상품 상세 조회
    // 성능 부하를 막기위해서 조회만 하는 경우에는 readOnly=true로 설정
    @Transactional(readOnly = true)
    public ItemDetailResponseDto getItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new BusinessLogicException(ResponseCode.ITEM_NOT_FOUND));
        return ItemDetailResponseDto.fromEntity(item);
    }

    // 상품 목록 조회
    @Transactional(readOnly = true)
    public ItemSearchResponseDto searchItems(ItemSearchRequestDto requestDto) {
//        Pageable pageable = requestDto.toPageable();
        return ItemSearchResponseDto.builder().build();
    }
}
