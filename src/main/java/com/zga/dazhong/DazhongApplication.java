package com.zga.dazhong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:application.properties", "classpath:spring-redis.properties"})
public class DazhongApplication {

    public static void main(String[] args) {
        SpringApplication.run(DazhongApplication.class, args);
    }

}
