package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCsvException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.QStudent;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.StudentDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonAddStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonImportStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.StudentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService implements PersonEditStrategy, PersonFindAllStrategy, PersonAddStrategy, PersonImportStrategy {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    @Qualifier("runStudent")
    private final Job job;
    private final JobLauncher jobLauncher;
    private String TEMP_STORAGE = "/src/main/resources/imports";
    private String TEMP_STORAGE_ABSOLUTE;

    @Override
    public PersonDto save(PersonCommand personCommand) {
        Student student = modelMapper.map(personCommand, Student.class);
        Student savedStudent = studentRepository.save(student);
        return modelMapper.map(savedStudent, StudentDto.class);
    }

    @Transactional
    @Override
    public void edit(String id, PersonEditCommand command) {
        Student student = modelMapper.map(command, Student.class);
//        student.setId(UUID.fromString(id));
        studentRepository.saveAndFlush(student);
    }
    @Override
    public List<StudentDto> findAll(Map<String, String> params, Pageable pageable) {
        QStudent student = QStudent.student;
        BooleanExpression predicate = buildPredicate(params, student);

        return studentRepository.findAll(predicate, pageable)
                .map(person -> modelMapper.map(person, StudentDto.class))
                .toList();
    }

    private BooleanExpression buildPredicate(Map<String, String> params, QStudent student) {
        BooleanExpression predicate = student.isNotNull();

        if (params.containsKey("firstName") && !params.get("firstName").isEmpty()) {
            predicate = predicate.and(student.firstName.equalsIgnoreCase(params.get("firstName")));
        }
        if (params.containsKey("lastName") && !params.get("lastName").isEmpty()) {
            predicate = predicate.and(student.lastName.equalsIgnoreCase(params.get("lastName")));
        }
        if (params.containsKey("peselNumber") && !params.get("peselNumber").isEmpty()) {
            predicate = predicate.and(student.peselNumber.equalsIgnoreCase(params.get("peselNumber")));
        }
        if (params.containsKey("heightFrom") && !params.get("heightFrom").isEmpty()) {
            double heightFrom = Double.parseDouble(params.get("heightFrom"));
            predicate = predicate.and(student.height.goe(heightFrom));
        }
        if (params.containsKey("heightTo") && !params.get("heightTo").isEmpty()) {
            double heightTo = Double.parseDouble(params.get("heightTo"));
            predicate = predicate.and(student.height.loe(heightTo));
        }
        if (params.containsKey("weightFrom") && !params.get("weightFrom").isEmpty()) {
            double weightFrom = Double.parseDouble(params.get("weightFrom"));
            predicate = predicate.and(student.weight.goe(weightFrom));
        }
        if (params.containsKey("weightTo") && !params.get("weightTo").isEmpty()) {
            double weightTo = Double.parseDouble(params.get("weightTo"));
            predicate = predicate.and(student.weight.loe(weightTo));
        }
        if (params.containsKey("email") && !params.get("email").isEmpty()) {
            predicate = predicate.and(student.email.equalsIgnoreCase(params.get("email")));
        }
        if (params.containsKey("college") && !params.get("college").isEmpty()) {
            predicate = predicate.and(student.college.equalsIgnoreCase(params.get("college")));
        }
        if (params.containsKey("academicYearFrom") && !params.get("academicYearFrom").isEmpty()) {
            int academicYearFrom = Integer.parseInt(params.get("academicYearFrom"));
            predicate = predicate.and(student.academicYear.goe(academicYearFrom));
        }
        if (params.containsKey("academicYearTo") && !params.get("academicYearTo").isEmpty()) {
            int academicYearTo = Integer.parseInt(params.get("academicYearTo"));
            predicate = predicate.and(student.academicYear.loe(academicYearTo));
        }
        if (params.containsKey("scholarshipFrom") && !params.get("scholarshipFrom").isEmpty()) {
            BigDecimal scholarshipFrom = new BigDecimal(params.get("scholarshipFrom"));
            predicate = predicate.and(student.scholarship.goe(scholarshipFrom));
        }
        if (params.containsKey("scholarshipTo") && !params.get("scholarshipTo").isEmpty()) {
            BigDecimal scholarshipTo = new BigDecimal(params.get("scholarshipTo"));
            predicate = predicate.and(student.scholarship.loe(scholarshipTo));
        }

        return predicate;
    }

    @Override
    public Long imports(MultipartFile multipartFile) {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String appPath = new File("").getAbsolutePath();
            TEMP_STORAGE_ABSOLUTE = Paths.get(appPath, TEMP_STORAGE) + "/";

            File fileToImport = new File(TEMP_STORAGE_ABSOLUTE + originalFileName);
            multipartFile.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", TEMP_STORAGE_ABSOLUTE + originalFileName)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            JobExecution job = jobLauncher.run(this.job, jobParameters);

            return job.getJobId();
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {

            throw new InvalidCsvException("Invalid csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
