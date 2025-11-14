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

    // 범용성을 위해 와일드카드를 활용함
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
