package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
