package com.moodeng.ezshop.dto.request;


import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.exception.BusinessLogicException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ItemSearchRequestDto {

    private String keyword;
    private String categoryName;
    private String filter;
    private Integer page; //조회할 페이지 번호 (Default: 1)
    private Integer perPage;
    private Integer sortedType; //정렬 기준(1 : 신상품순, 2 : 높은 가격순, 3 : 낮은 가격순) (Default: 1)

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
