package com.moodeng.ezshop.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class ItemSearchRequestDto {

    private String keyword;
    private String categoryName;
    private String filter;
    private Integer page; //조회할 페이지 번호 (Default: 1)
    private Integer perPage;
    private Integer sortedType; //정렬 기준(1 : 신상품순, 2 : 높은 가격순, 3 : 낮은 가격순) (Default: 1)

}
