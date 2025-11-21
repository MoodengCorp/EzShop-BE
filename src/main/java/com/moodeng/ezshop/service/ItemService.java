package com.moodeng.ezshop.service;

import com.moodeng.ezshop.constant.ItemStatus;
import com.moodeng.ezshop.dto.request.ItemCreateRequestDto;
import com.moodeng.ezshop.dto.request.ItemSearchRequestDto;
import com.moodeng.ezshop.dto.request.ItemUpdateRequestDto;
import com.moodeng.ezshop.dto.response.*;
import com.moodeng.ezshop.entity.Category;
import com.moodeng.ezshop.entity.Item;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.exception.BusinessLogicException;
import com.moodeng.ezshop.repository.CategoryRepository;
import com.moodeng.ezshop.repository.ItemRepository;
import com.moodeng.ezshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        Pageable pageable = requestDto.toPageable();

        Integer minPrice = requestDto.getMinPrice();
        Integer maxPrice = requestDto.getMaxPrice();

        // 일반 구매자가 검색하는 경우이므로 ItemStatus는 ACTIVE로 , sellerEmail은 null로 고정
        Page<Item> itemPage = itemRepository.findBySearchConditions(
                ItemStatus.ACTIVE,
                requestDto.getKeyword(),
                requestDto.getCategoryName(),
                minPrice,
                maxPrice,
                null,
                pageable
        );

        // repository에서 받아온 Page 객체에서 List<Item>을 받아온 후에 List<ItemSimpleResponseDto> 로 변환
        return buildSearchResponse(itemPage);
    }

    @Transactional(readOnly = true)
    public ItemSearchResponseDto searchMyItems(ItemSearchRequestDto requestDto, String sellerEmail) {
        Pageable pageable = requestDto.toPageable();

        Integer minPrice = requestDto.getMinPrice();
        Integer maxPrice = requestDto.getMaxPrice();

        // statusList가 null 인경우 jpql의 is null이 동작되어 전체 조회가 가능하도록 하기 위한 전처리
        ItemStatus status = requestDto.getItemStatus();

        // 판매자가 자신의 상품을 검색하는 경우에 ItemStatus를 동적으로 처리
        Page<Item> itemPage = itemRepository.findBySearchConditions(
                status,
                requestDto.getKeyword(),
                requestDto.getCategoryName(),
                minPrice,
                maxPrice,
                sellerEmail,
                pageable
        );

        // repository에서 받아온 Page 객체에서 List<Item>을 받아온 후에 List<ItemSimpleResponseDto> 로 변환
        return buildSearchResponse(itemPage);
    }

    @Transactional
    public void updateItem(Long itemId, ItemUpdateRequestDto requestDto,
                           MultipartFile thumbnailFile, MultipartFile detailImageFile,
                           String email){

        // update할 Item을 영속성컨텍스트에 올림
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->  new BusinessLogicException(ResponseCode.ITEM_NOT_FOUND));

        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new  BusinessLogicException(ResponseCode.SELLER_NOT_FOUND));

        // 해당 상품의 판매자인지 검토
        if (!item.getUser().getId().equals(seller.getId())) {
            throw new BusinessLogicException(ResponseCode.FORBIDDEN);
        }

        // SOLDOUT 상태는 재고를 0으로 만드는 방법으로만 설정가능
        if (requestDto.getStatus() != null && requestDto.getStatus() == ItemStatus.SOLDOUT) { // Enum은 == 으로 비교가능
            throw new BusinessLogicException(ResponseCode.INVALID_ITEM_STATUS_UPDATE);
        }

        // 카테고리명을 카테고리 객체로 치환
        Category category = null;
        if (StringUtils.hasText(requestDto.getCategoryName())) {
            category = categoryRepository.findByName(requestDto.getCategoryName())
                    .orElseThrow(() -> new  BusinessLogicException(ResponseCode.CATEGORY_NOT_FOUND));
        }

        String newThumbnailUrl = null;
        boolean thumbnailChanged = false;

        if(thumbnailFile != null && !thumbnailFile.isEmpty()){
            // 1. 새파일이 있다면 새 파일로 교체
            newThumbnailUrl = imageStorageService.updateFile(item.getThumbnailUrl(), thumbnailFile);
            thumbnailChanged = true;
        } else if(Boolean.TRUE.equals(requestDto.getRemoveThumbnail())){
            // 2. 파일을 삭제하고 싶다면 파일 삭제 후 DB에도 반영
            imageStorageService.deleteFile(item.getThumbnailUrl());
            thumbnailChanged = true;
        }

        String newDetailImageUrl = null;
        boolean detailImageChanged = false;

        if (detailImageFile != null && !detailImageFile.isEmpty()) {
            // 1. 새파일이 있다면 새 파일로 교체
            newDetailImageUrl = imageStorageService.updateFile(item.getDetailImageUrl(), detailImageFile);
            detailImageChanged = true;
        } else if (Boolean.TRUE.equals(requestDto.getRemoveDetailImage())) { // [수정] getRemoveDetailImage -> getRemoveDetailImage
            // 2. 파일을 삭제하고 싶다면 파일 삭제 후 DB에도 반영
            imageStorageService.deleteFile(item.getDetailImageUrl());
            detailImageChanged = true;
        }

        // 트랜잭션 종료시 더티체킹과 함께 DB에 반영
        item.update(requestDto, category,
                newThumbnailUrl,newDetailImageUrl,
                thumbnailChanged,detailImageChanged);

    }

    // page<Item>를 ItemSearchResponseDto로 변환하는 헬퍼 메서드
    private static ItemSearchResponseDto buildSearchResponse(Page<Item> itemPage) {
        List<ItemSimpleResponseDto> itemDtos = itemPage.getContent()
                .stream()
                .map(ItemSimpleResponseDto::fromEntity)
                .toList();

        // Page 객체를 Dto에 담음
        PaginationDto paginationDto = PaginationDto.fromPage(itemPage);

        // Item 리스트와 pagination 객체를 통해서 응답객체 조립
        return ItemSearchResponseDto.builder()
                .items(itemDtos)
                .pagination(paginationDto)
                .build();
    }
}
