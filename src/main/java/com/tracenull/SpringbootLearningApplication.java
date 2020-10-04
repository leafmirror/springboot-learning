package com.tracenull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringbootLearningApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootLearningApplication.class, args);
    }
}
