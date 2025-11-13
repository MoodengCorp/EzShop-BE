package com.moodeng.ezshop.entity;

import com.moodeng.ezshop.constant.DeliveryType;
import com.moodeng.ezshop.constant.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

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


}
