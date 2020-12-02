package com.transformers;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.transformers.mapper")
public class SpringbootShardingsphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShardingsphereApplication.class, args);
    }

}
