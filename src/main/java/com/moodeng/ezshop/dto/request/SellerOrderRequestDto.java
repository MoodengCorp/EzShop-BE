package com.moodeng.ezshop.dto.request;

import com.moodeng.ezshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SellerOrderRequestDto {
    private Integer page = 1;
    private Integer perPage = 10;
    private OrderStatus orderStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String itemName;
    private String buyerName;

    // 정렬기준은 생성일자 1개 밖에 없음
    public Pageable toPageable() {
        return PageRequest.of(page-1, perPage, Sort.by(Sort.Direction.DESC, "createdAt"));
    }


    // 추후 DB 조회를 위해서 시,분,초 까지 반영해주는 메서드
    public LocalDateTime getStartDateTime() {
        return startDate != null ? startDate.atStartOfDay() : null;
    }

    public LocalDateTime getEndDateTime() {
        return endDate != null ? endDate.atTime(23,59,59) : null;
    }

}
