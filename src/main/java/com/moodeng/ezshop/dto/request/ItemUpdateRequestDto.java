package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.DeliveryType;
import com.moodeng.ezshop.constant.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateRequestDto {

    private String name;
    private Integer price;
    private String categoryName;
    private Integer stockQuantity;
    private String origin;

    private ItemStatus status;
    private DeliveryType deliveryType;
    private String packagingType;
    private String salesUnit;
    private Integer weight;

}
