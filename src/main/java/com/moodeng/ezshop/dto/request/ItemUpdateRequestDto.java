package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.DeliveryType;
import com.moodeng.ezshop.constant.ItemStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateRequestDto {

    // 상품 내용이 수정되는 경우에는 사용자가 빈 문자열을 기입한 경우에
    // null로 들어오지 않고 빈문자열 그대로 들어오기때문에 제약조건 추가함
    @Size(min = 1, message = "상품명은 1글자 이상이어야 합니다.")
    private String name;

    @Min(value = 0, message = "상품의 가격은 0 이상이어야 합니다.")
    @Max(value = 100000000, message = "상품의 가격은 1억 이하여야 합니다.")
    private Integer price;
    private String categoryName;

    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    @Max(value = 100000000, message = "재고 수량은 1억 이하여야 합니다")
    private Integer stockQuantity;

    @Size(min = 1, message = "원산지는 1자 이상이어야 합니다.")
    private String origin;

    private ItemStatus status;
    private DeliveryType deliveryType;

    @Size(min = 1, message = "포장타입은 1자 이상이어야 합니다.")
    private String packagingType;

    @Size(min = 1, message = "판매단위는 1자 이상이어야 합니다.")
    private String salesUnit;

    @Min(value = 0, message = "중량은 0 이상이어야 합니다")
    private Integer weight;

    // 유저가 파일변경없이 파일을 삭제하고 싶은경우 아래 필드를 통해서 파일 삭제의사를 받아옴
    private Boolean removeThumbnail;
    private Boolean removeDetailImage;

}
