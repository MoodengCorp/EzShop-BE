package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.DeliveryType;
import com.moodeng.ezshop.constant.ItemStatus;
import com.moodeng.ezshop.entity.Category;
import com.moodeng.ezshop.entity.Item;
import com.moodeng.ezshop.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ItemCreateRequestDto {

    @NotBlank(message = "상품명 필수 기입값입니다.")
    private String name;

    @NotNull(message = "가격 필수 기입값입니다.")
    @Min(value = 0, message = "상품의 가격은 0 이상이어야 합니다.")
    @Max(value = 100000000, message = "상품의 가격은 1억 이하여야 합니다.")
    private Integer price;

    // 카테고리 id가 아닌 categoryName이 클라이언트에서 날아옴
//    @NotNull(message = "카테고리는 필수 입력 값입니다.") // 카테고리 기능 구현 전까지 주석처리해두었음
    private String categoryName;

    @NotNull(message = "재고수량은 필수 기입값입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    @Max(value = 100000000, message = "재고 수량은 1억 이하여야 합니다")
    private Integer stockQuantity;

    private String origin;
    private DeliveryType deliveryType; // 샛별배송, 일반배송 (Enum)
    private String packagingType; // 포장타입 (예: 냉장 (종이포장))
    private String salesUnit; // 판매단위 (예: 1EA)
    private Integer weight; // 중량 (예: 100)
    // 헬퍼 메서드
    public Item toEntity(User seller, Category category, String thumbnailUrl, String detailImageUrl) {
        return Item.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .origin(this.origin)
                .status(ItemStatus.ACTIVE) // 기본 상태는 '판매중'으로 설정
                .deliveryType(this.deliveryType)
                .packagingType(this.packagingType)
                .salesUnit(this.salesUnit)
                .weight(this.weight)
                .user(seller) // 파라미터로 받은 User 엔티티
                .category(category) // DTO로 받은 categoryId 정보를 바탕으로 DB에서 카테고리 정보를 가져옴
                .thumbnailUrl(thumbnailUrl) // 파라미터로 받은 URL
                .detailImageUrl(detailImageUrl) // 파라미터로 받은 URL
                .build();
    }

}

/*
{
		"name" : "사천식 파오차이 김치말이국수",
		"price" : 7000,
		"categoryName" : "가공식품",
		"stockQuantity" : 20,
		"thumbnailFile" : 이미지 파일,
		"detailImageFile" : 이미지 파일,
		"origin" : "중국",
}
 */