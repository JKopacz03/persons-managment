package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidWorkDateException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.QEmployee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.EmployeeDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PositionDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonAddStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonImportStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.EmployeeRepository;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PositionRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService implements PersonEditStrategy, PersonFindAllStrategy, PersonAddStrategy, PersonImportStrategy {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;
    @Qualifier("runEmployee")
    private final Job job;
    private final JobLauncher jobLauncher;
    private final EntityManager entityManager;
    private String TEMP_STORAGE = "/src/main/resources/imports/";
    private String TEMP_STORAGE_ABSOLUTE;

    @Override
    public PersonDto save(PersonCommand personCommand) {
        Employee employee = modelMapper.map(personCommand, Employee.class);
        Employee savedEntity = employeeRepository.save(employee);
        return modelMapper.map(savedEntity, EmployeeDto.class);
    }

    @Override
    @Transactional
    public void edit(Long id, PersonEditCommand command) {
        Employee employee = modelMapper.map(command, Employee.class);
        employeeRepository.saveAndFlush(employee);
    }

    @Override
    @Transactional
    public List<EmployeeDto> findAll(Map<String, String> params, Pageable pageable) {
        QEmployee employee = QEmployee.employee;

        BooleanExpression predicate = buildPredicate(params, employee);

        return new JPAQuery<>(entityManager)
                .select(Projections.constructor(EmployeeDto.class,
                        employee.id,
                        employee.firstName,
                        employee.lastName,
                        employee.peselNumber,
                        employee.height,
                        employee.weight,
                        employee.email,
                        employee.workStartDate,
                        employee.actualProfession,
                        employee.salary,
                        employee.positions.size()))
                .from(employee)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression buildPredicate(Map<String, String> params, QEmployee employee) {
        BooleanExpression predicate = employee.isNotNull();

        if (params.containsKey("firstName") && !params.get("firstName").isEmpty()) {
            predicate = predicate.and(employee.firstName.equalsIgnoreCase(params.get("firstName")));
        }
        if (params.containsKey("lastName") && !params.get("lastName").isEmpty()) {
            predicate = predicate.and(employee.lastName.equalsIgnoreCase(params.get("lastName")));
        }
        if (params.containsKey("peselNumber") && !params.get("peselNumber").isEmpty()) {
            predicate = predicate.and(employee.peselNumber.equalsIgnoreCase(params.get("peselNumber")));
        }
        if (params.containsKey("heightFrom") && !params.get("heightFrom").isEmpty()) {
            double heightFrom = Double.parseDouble(params.get("heightFrom"));
            predicate = predicate.and(employee.height.goe(heightFrom));
        }
        if (params.containsKey("heightTo") && !params.get("heightTo").isEmpty()) {
            double heightTo = Double.parseDouble(params.get("heightTo"));
            predicate = predicate.and(employee.height.loe(heightTo));
        }
        if (params.containsKey("weightFrom") && !params.get("weightFrom").isEmpty()) {
            double weightFrom = Double.parseDouble(params.get("weightFrom"));
            predicate = predicate.and(employee.weight.goe(weightFrom));
        }
        if (params.containsKey("weightTo") && !params.get("weightTo").isEmpty()) {
            double weightTo = Double.parseDouble(params.get("weightTo"));
            predicate = predicate.and(employee.weight.loe(weightTo));
        }
        if (params.containsKey("email") && !params.get("email").isEmpty()) {
            predicate = predicate.and(employee.email.equalsIgnoreCase(params.get("email")));
        }
        if (params.containsKey("workStartDateFrom") && !params.get("workStartDateFrom").isEmpty()) {
            LocalDate workStartDateFrom = LocalDate.parse(params.get("workStartDateFrom"));
            predicate = predicate.and(employee.workStartDate.goe(workStartDateFrom));
        }
        if (params.containsKey("workStartDateTo") && !params.get("workStartDateTo").isEmpty()) {
            LocalDate workStartDateTo = LocalDate.parse(params.get("workStartDateTo"));
            predicate = predicate.and(employee.workStartDate.loe(workStartDateTo));
        }
        if (params.containsKey("actualProfession") && !params.get("actualProfession").isEmpty()) {
            predicate = predicate.and(employee.actualProfession.equalsIgnoreCase(params.get("actualProfession")));
        }
        if (params.containsKey("salaryFrom") && !params.get("salaryFrom").isEmpty()) {
            BigDecimal salaryFrom = new BigDecimal(params.get("salaryFrom"));
            predicate = predicate.and(employee.salary.goe(salaryFrom));
        }
        if (params.containsKey("salaryTo") && !params.get("salaryTo").isEmpty()) {
            BigDecimal salaryTo = new BigDecimal(params.get("salaryTo"));
            predicate = predicate.and(employee.salary.loe(salaryTo));
        }
        if (params.containsKey("numberOfPositionsFrom") && !params.get("numberOfPositionsFrom").isEmpty()) {
            long numberOfPositionsFrom = Long.parseLong(params.get("numberOfPositionsFrom"));
            predicate = predicate.and(employee.positions.size().goe(numberOfPositionsFrom));
        }
        if (params.containsKey("numberOfPositionsTo") && !params.get("numberOfPositionsTo").isEmpty()) {
            long numberOfPositionsTo = Long.parseLong(params.get("numberOfPositionsTo"));
            predicate = predicate.and(employee.positions.size().loe(numberOfPositionsTo));
        }

        return predicate;
    }


    @Transactional
    public PositionDto addPosition(PositionCommand command) {
        if(command.getStartDate().isAfter(LocalDate.now()) || command.getEndDate().isAfter(LocalDate.now())){
            throw new InvalidWorkDateException("dates must cannot be in future!");
        }
        if(!command.getStartDate().isBefore(command.getEndDate())){
            throw new InvalidWorkDateException("end date must be after start date!");
        }
        Employee employee = employeeRepository.findByPeselNumberWithPessimisticLock(command.getPeselNumber()).orElseThrow();
        if(!command.getEndDate().isBefore(employee.getWorkStartDate())){
            throw new InvalidWorkDateException("your dates are collide with actual position");
        }
        if(employee.getPositions().stream()
                .anyMatch(e -> (!command.getStartDate().isAfter(e.getEndDate()) && !command.getStartDate().isBefore(e.getStartDate()))
            || (command.getStartDate().isBefore(e.getStartDate()) && !command.getEndDate().isBefore(e.getStartDate())))){
            throw new InvalidWorkDateException("your dates are collide with another positions");
        }
        Position position = modelMapper.map(command, Position.class);
        position.setEmployee(employee);
        Position savedPosition = positionRepository.save(position);
        return modelMapper.map(savedPosition, PositionDto.class);
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
                 JobParametersInvalidException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}


