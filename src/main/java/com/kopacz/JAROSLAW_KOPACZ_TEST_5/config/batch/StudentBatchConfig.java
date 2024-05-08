package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.processors.StudentProcessor;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class StudentBatchConfig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final DataSource dataSource;

    @Bean
    @StepScope
    public CustomFlatFileItemReader<Student> studentItemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile){
        CustomFlatFileItemReader<Student> itemReader = new CustomFlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(new File(pathToFile)));
        itemReader.setName("csvStudentReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(studentLineMapper());
        return itemReader;
    }

    @Bean
    public StudentProcessor studentProcessor(){
        return new StudentProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Student> studentWriterJdbc() {
        JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO person (type, id, first_name, last_name, pesel_number, height, weight, email, version, college, academic_year, scholarship)" +
                "VALUES (:type, :id, :firstName, :lastName, :peselNumber, :height, :weight, :email, :version, :college, :academicYear, :scholarship)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

    @Bean
    public Step importStudentStep(CustomFlatFileItemReader<Student> fileReader) {
        return new StepBuilder("csvPersonImport", jobRepository)
                .<Student, Student>chunk(100000, platformTransactionManager)
                .reader(fileReader)
                .processor(studentProcessor())
                .writer(studentWriterJdbc())
                .build();
    }

    @Bean
    @Qualifier("runStudent")
    public Job runStudent(CustomFlatFileItemReader<Student> fileReader){
        return new JobBuilder("importStudents", jobRepository)
                .start(importStudentStep(fileReader))
                .build();
    }

    private LineMapper<Student> studentLineMapper(){
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("type", "id", "firstName", "lastName", "peselNumber", "height",
                "weight", "email", "version", "college", "academicYear", "scholarship");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
