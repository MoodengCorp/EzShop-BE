package com.moodeng.ezshop.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class SellerOrderListResponseDto {
    private List<SellerOrderResponseDto>  sellerOrderList;
    private PaginationDto pagination;
    public static SellerOrderListResponseDto of(List<SellerOrderResponseDto> sellerOrderList, Page<?> page){
        return SellerOrderListResponseDto.builder()
                .sellerOrderList(sellerOrderList)
                .pagination(PaginationDto.fromPage(page))
                .build();
    }
}
