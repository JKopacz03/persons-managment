package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

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
    private final DataSource dataSource;

    @Bean
    @StepScope
    public CustomFlatFileItemReader<Pensioner> pensionerItemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
        CustomFlatFileItemReader<Pensioner> itemReader = new CustomFlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(new File(pathToFile)));
        itemReader.setName("csvPensionerReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(pensionerLineMapper());
        return itemReader;
    }

    @Bean
    public PensionerProcessor pensionerProcessor(){
        return new PensionerProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Pensioner> pensionerWriterJdbc() {
        JdbcBatchItemWriter<Pensioner> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO person (type, id, first_name, last_name, pesel_number, height, weight, email, version, pension_value, work_years)" +
                "VALUES (:type, :id, :firstName, :lastName, :peselNumber, :height, :weight, :email, :version, :pensionValue, :workYears)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

    @Bean
    public Step importPensionerStep(CustomFlatFileItemReader<Pensioner> fileReader) {
        return new StepBuilder("csvEmployeeImport", jobRepository)
                .<Pensioner, Pensioner>chunk(100000, platformTransactionManager)
                .reader(fileReader)
                .processor(pensionerProcessor())
                .writer(pensionerWriterJdbc())
                .build();
    }
    @Bean
    @Qualifier("runPensioner")
    public Job runPensioner(CustomFlatFileItemReader<Pensioner> fileReader){
        return new JobBuilder("importPensioners", jobRepository)
                .start(importPensionerStep(fileReader))
                .build();
    }

    private LineMapper<Pensioner> pensionerLineMapper(){
        DefaultLineMapper<Pensioner> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("type", "id", "firstName", "lastName", "peselNumber", "height",
                "weight", "email", "version", "pensionValue", "workYears");

        BeanWrapperFieldSetMapper<Pensioner> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Pensioner.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
