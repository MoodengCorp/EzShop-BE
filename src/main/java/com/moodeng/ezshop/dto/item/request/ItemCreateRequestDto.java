package com.moodeng.ezshop.dto.item.request;

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

    @NotBlank(message = "상품명 필수 기입.")
    private String name;

    @NotNull(message = "가격 필수 기입.")
    @Min(value = 0, message = "0 이상의 가격 기입")
    @Max(value = 100000000, message = "1억 이하의 가격 기입")
    private Integer price;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Long categoryId;

    @NotNull(message = "재고수량 필수 기입.")
    @Min(value = 0, message = "0 이상의 수량 기입")
    private Integer stockQuantity;
    private MultipartFile thumbnailFile;
    private MultipartFile detailImageFile;
    private String origin;
    
    // 헬퍼 메서드
    public Item toEntity(User seller, Category category, String thumbnailUrl, String detailImageUrl) {
        return Item.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .origin(this.origin)
                .user(seller) // 파라미터로 받은 User 엔티티
                .category(category) // DTO로 받은 categoryId 정보를 바탕으로 DB에서 카테고리 정보를 가져옴
                .thumbnailUrl(thumbnailUrl) // 파라미터로 받은 URL
                .detailImageUrl(detailImageUrl) // 파라미터로 받은 URL
                .status(ItemStatus.ACTIVE) // 기본 상태는 '판매중'으로 설정
                .build();
    }

}

/*
{
		"name" : "사천식 파오차이 김치말이국수",
		"price" : 7000,
		"categoryId" : 5,
		"stockQuantity" : 20,
		"thumbnailFile" : 이미지 파일,
		"detailImageFile" : 이미지 파일,
		"origin" : "중국",
}
 */