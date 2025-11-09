package com.moodeng.ezshop.entity;

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

//    중량은 일단 기입안했음
//    private Integer weight;
    
    @Column(name = "price",  nullable = false)
    private Integer price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    private ItemStatus status;

    @Column(name = "detail_image_url", length = 255)
    private String detailImageUrl;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;


}
