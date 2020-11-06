package com.tracenull.config;

import com.tracenull.domain.User2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class User2Config {
    @Bean
    public User2 user2() {
        return new User2();
    }
}
