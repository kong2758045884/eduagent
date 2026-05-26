package com.innovation.training;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.innovation.training.module.user.mapper")
@SpringBootApplication
public class TrainingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingBackendApplication.class, args);
    }
}
