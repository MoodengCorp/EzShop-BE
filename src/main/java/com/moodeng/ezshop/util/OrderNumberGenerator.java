package com.moodeng.ezshop.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderNumberGenerator {

    // 주문 번호 형식: ORD-yyyyMMdd-HHmmssSSS-XXXX (예: ORD-20250721-123055123-1234)
    public static String generateOrderNumber(){
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS"));
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        return String .format("ORD-%s-%s", timestamp, randomNum);
    }
}
