package com.transformers.springcloud.hmily.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.transformers.springcloud.hmily.order.dao")
public class SpringCloudHmilyOrderApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SpringCloudHmilyOrderApplication.class, args);
    }

}
