package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.processors.EmployeeProcessor;
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
public class EmployeeBatchConfig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final DataSource dataSource;

    @Bean
    @StepScope
    public CustomFlatFileItemReader<Employee> employeeItemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile){
        CustomFlatFileItemReader<Employee> itemReader = new CustomFlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(new File(pathToFile)));
        itemReader.setName("csvEmployeeReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(employeeLineMapper());
        return itemReader;
    }

    @Bean
    public EmployeeProcessor employeeProcessor(){
        return new EmployeeProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Employee> employeeWriterJdbc() {
        JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO person (type, id, first_name, last_name, pesel_number, height, weight, email, version, work_start_date, actual_profession, salary, number_of_professions)" +
                "VALUES (:type, :id, :firstName, :lastName, :peselNumber, :height, :weight, :email, :version, :workStartDate, :actualProfession, :salary, :numberOfProfessions)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

    @Bean
    public Step importEmployeeStep(CustomFlatFileItemReader<Employee> fileReader) {
        return new StepBuilder("csvEmployeeImport", jobRepository)
                .<Employee, Employee>chunk(100000, platformTransactionManager)
                .reader(fileReader)
                .processor(employeeProcessor())
                .writer(employeeWriterJdbc())
                .build();
    }

    @Bean
    @Qualifier("runEmployee")
    public Job runEmployee(CustomFlatFileItemReader<Employee> fileReader){
        return new JobBuilder("importEmployees", jobRepository)
                .start(importEmployeeStep(fileReader))
                .build();
    }

    private LineMapper<Employee> employeeLineMapper(){
        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("type", "id", "firstName", "lastName", "peselNumber", "height",
                "weight", "email", "version", "workStartDate", "actualProfession", "salary", "numberOfProfessions");

        BeanWrapperFieldSetMapperCustom<Employee> fieldSetMapper = new BeanWrapperFieldSetMapperCustom<>();
        fieldSetMapper.setTargetType(Employee.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
