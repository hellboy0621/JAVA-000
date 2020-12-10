package com.transformer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.transformer.dao"})
public class SsProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsProxyApplication.class, args);
    }

}
