package com.moodeng.ezshop.entity;

import com.moodeng.ezshop.constant.DeliveryType;
import com.moodeng.ezshop.constant.ItemStatus;
import com.moodeng.ezshop.dto.request.ItemUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 테스트 편의를 위해 optional = false 설정은 추후 반영 예정
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY) // 테스트 편의를 위해 optional = false 설정은 추후 반영 예정
    @JoinColumn(name = "user_id", nullable = false) // ERD에는 null로 되어있는데 notnull로 함
    private User user; // 판매자인 user

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "origin", length = 50)
    private String origin;

    // 추가된 필드들
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType; // 샛별배송, 일반배송

    @Column(name = "packaging_type")
    private String packagingType; // 포장타입

    @Column(name = "sales_unit")
    private String salesUnit; // 판매단위

    @Column(name = "weight")
    private Integer weight; // 숫자만 저장되니까 프론트에서 단위처리는 프론트에서 통일하면 될 듯 (g으로?)

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
    @Column(name = "price",  nullable = false)
    private Integer price;


    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    private ItemStatus status;

    @Column(name = "detail_image_url", length = 255)
    private String detailImageUrl;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    // 상품 정보 수정 헬퍼 메서드 : dto의 정보를 영속성 컨텍스트에 올라온 item에 반영해줌
    // 코드가 지저분해진것 같아서 리플렉션으로 안될까 찾아보니 성능도 떨어지고 비즈니스 로직을 반영하기 어려워진다고 하네요.
    public void update(ItemUpdateRequestDto requestDto, Category newCategory,
                       String newThumbnailUrl, String newDetailImageUrl,
                       Boolean thumbnailChanged, boolean detailImageChanged) {

        if (requestDto.getName() != null) {
            this.name = requestDto.getName();
        }
        if (requestDto.getPrice() != null) {
            this.price = requestDto.getPrice();
        }

        if (requestDto.getOrigin() != null) {
            this.origin = requestDto.getOrigin();
        }
        if (requestDto.getPackagingType() != null) {
            this.packagingType = requestDto.getPackagingType();
        }
        if (requestDto.getSalesUnit() != null) {
            this.salesUnit = requestDto.getSalesUnit();
        }
        if (requestDto.getWeight() != null) {
            this.weight = requestDto.getWeight();
        }
        if (requestDto.getPrice() != null) {
            this.price = requestDto.getPrice();
        }
        if (requestDto.getDeliveryType() != null) {
            this.deliveryType = requestDto.getDeliveryType();
        }

        if (newCategory != null) {
            this.category = newCategory;
        }

        // 썸네일 변경의사가 있다면 변경
        if (thumbnailChanged){
            this.thumbnailUrl = newThumbnailUrl;
        }

        // 상세 정보 이미지 변경의사가 있다면 변경
        if (detailImageChanged){
            this.detailImageUrl = newDetailImageUrl;
        }

        if (requestDto.getStockQuantity() != null) {
            this.stockQuantity = requestDto.getStockQuantity();
        }

        // 재고가 0인경우 SOLDOUT으로 바꿔줌
        if (this.stockQuantity <= 0 && this.status != ItemStatus.HIDDEN) {
            this.status = ItemStatus.SOLDOUT;
        }

        // SOLDOUT 상태에서 재고가 0이 아니게 되면 ACTIVE로 변경
        // 사용자가 item을 업데이트할때 HIDDEN을 선택한 경우 if문의 순서로 인해서 ACTIVE로 전환 안됨
        if (this.status == ItemStatus.SOLDOUT && this.stockQuantity > 0) {
            this.status = ItemStatus.ACTIVE;
        }


    }

}
