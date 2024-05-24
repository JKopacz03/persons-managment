package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch.tasklet.EmployeeTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class EmployeeBatchConfig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final EmployeeTasklet employeeTasklet;

    @Bean
    public Step importEmployeeStep() {
        return new StepBuilder("csvEmployeeImport", jobRepository)
                .tasklet(employeeTasklet, platformTransactionManager)
                .build();
    }
    @Bean
    @Qualifier("runEmployee")
    public Job runEmployee(){
        return new JobBuilder("importEmployees", jobRepository)
                .start(importEmployeeStep())
                .build();
    }
}
