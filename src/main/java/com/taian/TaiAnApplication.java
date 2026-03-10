package com.taian;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaiAnApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaiAnApplication.class, args);
    }

}
