package com.moodeng.ezshop.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Period {
    THREE_MONTHS("3개월", 3),
    SIX_MONTHS("6개월", 3),
    ONE_YEAR("1년",12),
    ALL("전체", -1); // 내부 로직으로 처리할거라서 -1 이라는 의미없는 값으로 넣음

    private final String stringPeriod;
    private final Integer monthsToSubtract;

    public static Period fromString(String stringPeriod) {
        if (stringPeriod == null || stringPeriod.isEmpty()) {
            return ALL;
        }
        return Arrays.stream(Period.values())
                .filter(period -> period.stringPeriod.equals(stringPeriod))
                .findFirst()
                .orElse(ALL);
    }

    public LocalDateTime calculateStartDate(){
        if (this == ALL) {
            return LocalDateTime.of(1900,1,1,0,0);
        }
        return LocalDateTime.now().minusMonths(monthsToSubtract);
    }
}
