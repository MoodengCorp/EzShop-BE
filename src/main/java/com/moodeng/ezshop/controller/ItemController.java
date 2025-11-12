package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.item.request.ItemCreateRequestDto;
import com.moodeng.ezshop.dto.item.request.ItemSearchRequestDto;
import com.moodeng.ezshop.dto.item.response.ItemDetailResponseDto;
import com.moodeng.ezshop.dto.item.response.ItemSearchResponseDto;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor // 의존성 주입까지 자동으로 해줌
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;


    // 상품 생성
    @PostMapping
    public ResponseEntity<CommonResponse<Void>> createItem(
            // MultiPartFile을 받기위해 @RequestPart 사용
            @Valid @RequestPart("dto") ItemCreateRequestDto itemCreateRequestDto,
            @RequestPart(value="thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestPart(value="detailImageFile", required = false) MultipartFile detailImageFile,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        // Controller에서 user의 권한 정보를 처리하는게 나아보여서,
        // controller에서 user정보까지 포함된 entity를 조립해서 service에 보내는 방향으로 수정 고민중
        // UserDetail에서 현재는 email만 가져오는 것 같은데 id를 가져오는 방법은 없을지?
        Long sellerId = 1L; // 임시 값
        itemService.createItem(itemCreateRequestDto, thumbnailFile, detailImageFile, sellerId);
        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    // 상품 상세 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<CommonResponse<ItemDetailResponseDto>> getItemDetail(
            @PathVariable Long itemId
    ){
        ItemDetailResponseDto responseDto = itemService.getItemDetail(itemId);
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse<ItemSearchResponseDto>> searchItems(
            // 쿼리 파라미터를 처리해주기 위해서 @ModelAttribute 사용
            @Valid @ModelAttribute ItemSearchRequestDto requestDto
            ){

        // 필터링 조건은 String 그대로 service에 넘김 -> service에서 처리 예정
        ItemSearchResponseDto responseDto = itemService.searchItems(requestDto);
        return ResponseEntity.ok(CommonResponse.ofSuccess(responseDto));
    }
}
