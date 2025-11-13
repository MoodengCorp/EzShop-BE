package com.moodeng.ezshop.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PaginationDto {

    private final Integer page;
    private final Integer perPage;
    private final Integer totalPage;
    private final Long totalCount;
    private final boolean hasNext;
    private final boolean hasPrev;

    public static PaginationDto fromPage(Page<?> page){

        return PaginationDto.builder()
                .page(page.getNumber() + 1) // Page는 0-based index이므로 +1
                .perPage(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrev(page.hasPrevious())
                .build();
    }
}
