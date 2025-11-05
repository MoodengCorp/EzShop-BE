package com.moodeng.ezshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EzShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzShopApplication.class, args);
    }

}
