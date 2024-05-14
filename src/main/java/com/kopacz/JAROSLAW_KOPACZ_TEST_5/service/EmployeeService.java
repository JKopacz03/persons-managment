package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidWorkDateException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.EmployeeFindCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.PersonFindCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.EmployeeDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.EmployeeRepository;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PositionRepository;
import org.springframework.batch.core.*;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification.EmployeeSpecification.*;

@Service
public class EmployeeService implements PersonEditStrategy, PersonFindAllStrategy {
    private EmployeeRepository employeeRepository;
    private PositionRepository positionRepository;
    private ModelMapper modelMapper;
    private Job job;
    private JobLauncher jobLauncher;
    private String TEMP_STORAGE = "/src/main/resources/imports/";
    private String TEMP_STORAGE_ABSOLUTE;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PositionRepository positionRepository, ModelMapper modelMapper, @Qualifier("runEmployee") Job job, JobLauncher jobLauncher) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.modelMapper = modelMapper;
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    @Override
    @Transactional
    public void edit(String id, PersonEditCommand command) {
        Employee employee = modelMapper.map(command, Employee.class);
        employee.setId(UUID.fromString(id));
        employeeRepository.saveAndFlush(employee);
    }

    @Override
    @Transactional
    public List<EmployeeDto> findAll(
            PersonFindCommand personFindCommand,
            Pageable pageable) {

        EmployeeFindCommand employeeFindCommand = modelMapper.map(personFindCommand, EmployeeFindCommand.class);

        String firstName = employeeFindCommand.getFirstName();
        String lastName = employeeFindCommand.getLastName();
        String peselNumber = employeeFindCommand.getPeselNumber();
        double heightFrom = employeeFindCommand.getHeightFrom();
        double heightTo = employeeFindCommand.getHeightTo();
        double weightFrom = employeeFindCommand.getWeightFrom();
        double weightTo = employeeFindCommand.getWeightTo();
        String email = employeeFindCommand.getEmail();
        LocalDate workStartDateFrom = employeeFindCommand.getWorkStartDateFrom();
        LocalDate workStartDateTo = employeeFindCommand.getWorkStartDateTo();
        String actualProfession = employeeFindCommand.getActualProfession();
        BigDecimal salaryFrom = employeeFindCommand.getSalaryFrom();
        BigDecimal salaryTo = employeeFindCommand.getSalaryTo();
        int numberOfProfessionsFrom = employeeFindCommand.getNumberOfProfessionsFrom();
        int numberOfProfessionsTo = employeeFindCommand.getNumberOfProfessionsTo();


        Specification<Employee> filters = Specification.where(
                        StringUtils.isBlank(firstName) ? null : byFirstName(firstName))
                .and(StringUtils.isBlank(lastName) ? null : byLastName(lastName))
                .and(StringUtils.isBlank(peselNumber) ? null : byPeselNumber(peselNumber))
                .and(heightFrom > 0 ? byHeightFrom(heightFrom) : null)
                .and(heightTo > 0 ? byHeightTo(heightTo) : null)
                .and(weightFrom > 0 ? byWeightFrom(weightFrom) : null)
                .and(weightTo > 0 ? byWeightTo(weightTo) : null)
                .and(StringUtils.isBlank(email) ? null : byEmail(email))
                .and(Objects.isNull(workStartDateFrom) ? null : byWorkStartDateFrom(workStartDateFrom))
                .and(Objects.isNull(workStartDateTo) ? null : byWorkStartDateTo(workStartDateTo))
                .and(StringUtils.isBlank(actualProfession) ? null : byActualProfession(actualProfession))
                .and(Objects.isNull(salaryFrom) ? null : bySalaryFrom(salaryFrom))
                .and(Objects.isNull(salaryTo) ? null : bySalaryTo(salaryTo))
                .and(numberOfProfessionsFrom > 0 ? byNumberOfProfessionsFrom(numberOfProfessionsFrom) : null)
                .and(numberOfProfessionsTo > 0 ? byNumberOfProfessionsTo(numberOfProfessionsTo) : null);

            return employeeRepository.findAll(filters, pageable)
                    .map(person -> {
                        EmployeeDto dto = modelMapper.map(person, EmployeeDto.class);
                        dto.setNumberOfPositions(person.getPositions().size());
                        return dto;
                    })
                    .toList();

    }

    @Transactional
    public void addPosition(String peselNumber, PositionCommand command) {
        if(!command.getStartDate().isBefore(command.getEndDate())){
            throw new InvalidWorkDateException("end date must be after start date!");
        }
        Employee employee = employeeRepository.findByPeselNumberWithPessimisticLock(peselNumber).orElseThrow();
        if(employee.getPositions().stream()
                .anyMatch(e -> !command.getStartDate().isAfter(e.getEndDate())
                        && !command.getEndDate().isBefore(e.getStartDate()))){
            throw new InvalidWorkDateException("your dates are collide with another positions");
        }
        Position position = modelMapper.map(command, Position.class);
        position.setEmployee(employee);
        positionRepository.save(position);
    }

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


