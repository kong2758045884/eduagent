package com.innovation.training;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@MapperScan({
        "com.innovation.training.module.user.mapper",
        "com.innovation.training.module.lesson.mapper",
        "com.innovation.training.module.diagnosis.mapper",
        "com.innovation.training.module.resource.mapper",
        "com.innovation.training.module.research.mapper",
        "com.innovation.training.module.file.mapper",
        "com.innovation.training.module.growth.mapper",
        "com.innovation.training.module.ai.mapper",
        "com.innovation.training.module.expert.mapper",
        "com.innovation.training.module.qa.mapper",
        "com.innovation.training.module.casebase.mapper",
        "com.innovation.training.module.classroom",
        "com.innovation.training.module.notification.mapper",
        "com.innovation.training.module.course.mapper"
})
@ConfigurationPropertiesScan
@SpringBootApplication
public class TrainingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingBackendApplication.class, args);
    }
}
