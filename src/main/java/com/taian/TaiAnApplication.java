package com.taian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.taian.mapper")
@SpringBootApplication
public class TaiAnApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaiAnApplication.class, args);
    }

}
