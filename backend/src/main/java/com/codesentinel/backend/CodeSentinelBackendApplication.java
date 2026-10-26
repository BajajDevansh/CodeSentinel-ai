package com.codesentinel.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoAuditing
@EnableAsync
public class CodeSentinelBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodeSentinelBackendApplication.class, args);
    }
}
