package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch.tasklet.StudentTasklet;
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
public class StudentBatchConfig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final StudentTasklet studentTasklet;

    @Bean
    public Step importStudentStep() {
        return new StepBuilder("csvStudentImport", jobRepository)
                .tasklet(studentTasklet, platformTransactionManager)
                .build();
    }
    @Bean
    @Qualifier("runStudent")
    public Job runStudent(){
        return new JobBuilder("importStudents", jobRepository)
                .start(importStudentStep())
                .build();
    }
}