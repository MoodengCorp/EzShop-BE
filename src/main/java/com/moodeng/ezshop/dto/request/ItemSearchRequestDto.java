package com.moodeng.ezshop.dto.request;


import com.moodeng.ezshop.constant.ItemStatus;
import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.exception.BusinessLogicException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@Setter
public class ItemSearchRequestDto {

    private String keyword;
    private String categoryName;
    private String filter;

    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private Integer page = 1; //조회할 페이지 번호 (Default: 1)

    @Min(value = 1, message = "페이지 당 항목 수는 1 이상이어야 합니다.")
    private Integer perPage = 10; // 기본값 10
    private Integer sortedType = 1; //정렬 기준(1 : 신상품순, 2 : 높은 가격순, 3 : 낮은 가격순) (Default: 1)
    
    // seller가 자기상품을 조회하는 경우가 추가되면서 일반유저와 판매자가 볼 수 있는 아이템상태가 다르므로 ItemStatus필드를 추가함
    // 여러개를 선택할 수 있으므로 list로 받음
    private List<ItemStatus> itemStatus;


    public Pageable toPageable(){
        Sort sort;

        // 정렬기준 2 -> 가격 내림차순
        if (this.sortedType != null && this.sortedType == 2){
            sort = Sort.by(Sort.Direction.DESC, "price");

            // 정렬기준 3 -> 가격 오름차순
        } else if (this.sortedType != null && this.sortedType == 3) {
            sort = Sort.by(Sort.Direction.ASC, "price");

            // 정렬기준 1 또는 그외 -> 최신순
        } else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        return PageRequest.of(this.page - 1, this.perPage, sort);
    }

    //최소값 반환
    public Integer getMinPrice(){
        return parsePriceFromFilter(0);
    }

    //최대값 반환
    public Integer getMaxPrice(){
        return parsePriceFromFilter(1);
    }

    private Integer parsePriceFromFilter(int index) {
        if (!StringUtils.hasText(this.filter) || !this.filter.startsWith("price:")) {
            return null;
        }
        try {

            // 앞에 "price:" 떼기
            String priceRangeStr = this.filter.substring("price:".length());

            /*
            - 최대값 또는 최소값만 있는 경우를 처리하기 위해서 limit을 인자로 사용함
            - split 할때 두번째 인자인 limit 값을 -1로 두면 빈 문자열을 남겨둔다고 합니다
            예) price:10000- -> ["10000",""]
             */
            String[] prices = priceRangeStr.split("-",-1);

            // 숫자가 2개 들어온게 아니라
            if (prices.length != 2) {
                throw new Exception();
            }

            // 선택한 인덱스의 최소, 최대값이 없는 경우
            if (!StringUtils.hasText(prices[index])){
                return null;
            }

            //String을 integer로 변환해서 반환
            return Integer.parseInt(prices[index]);

        } catch (Exception e) {
            // 필터 형식이 안맞는다면 커스텀 예외를 던짐
            throw new BusinessLogicException(ResponseCode.INVALID_FILTER_FORMAT);
        }

    }

}
