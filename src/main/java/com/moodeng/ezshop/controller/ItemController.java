package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.item.request.ItemCreateRequestDto;
import com.moodeng.ezshop.dto.item.request.ItemSearchRequestDto;
import com.moodeng.ezshop.dto.item.response.ItemDetailResponseDto;
import com.moodeng.ezshop.dto.item.response.ItemSearchResponseDto;
import com.moodeng.ezshop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // 의존성 주입까지 자동으로 해줌
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;


    // 상품 생성
    @PostMapping
    public ResponseEntity<String> createItem(
            // MultiPartFile을 받기위해 @ModelAttribute 사용
            @Valid @ModelAttribute ItemCreateRequestDto itemCreateRequestDto
    ) {

        // Controller에서 user의 권한 정보를 처리하는게 나아보여서,
        // controller에서 user정보까지 포함된 entity를 조립해서 service에 보내는 방향으로 수정 고민중
        // 현재는 유저 정보는 취급안하는 코드임
        Long sellerId = 1L; // 임시 값
        itemService.createItem(itemCreateRequestDto, sellerId);
        return ResponseEntity.ok("상품이 등록되었습니다.");
    }

    // 상품 상세 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDetailResponseDto> getItemDetail(
            @PathVariable Long itemId
    ){
        ItemDetailResponseDto responseDto = itemService.getItemDetail(itemId);
        return ResponseEntity.ok(responseDto);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<ItemSearchResponseDto> searchItems(
            // 쿼리 파라미터를 처리해주기 위해서 @ModelAttribute 사용
            @Valid @ModelAttribute ItemSearchRequestDto requestDto
            ){

        ItemSearchResponseDto responseDto = itemService.searchItems(requestDto);
        return ResponseEntity.ok(responseDto);
    }




}
