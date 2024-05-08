package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config;

import org.modelmapper.ModelMapper;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class AppConfig {
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
