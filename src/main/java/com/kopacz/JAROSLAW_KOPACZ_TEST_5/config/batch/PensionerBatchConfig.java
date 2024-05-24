package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch.tasklet.PensionerTasklet;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Pensioner;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.processors.PensionerProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PensionerBatchConfig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final PensionerTasklet pensionerTasklet;

    @Bean
    public Step importPensionerStep() {
        return new StepBuilder("csvPensionerImport", jobRepository)
                .tasklet(pensionerTasklet, platformTransactionManager)
                .build();
    }
    @Bean
    @Qualifier("runPensioner")
    public Job runPensioner(){
        return new JobBuilder("importPensioners", jobRepository)
                .start(importPensionerStep())
                .build();
    }
}
