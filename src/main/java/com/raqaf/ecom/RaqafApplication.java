package com.raqaf.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class RaqafApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaqafApplication.class, args);
    }

}
