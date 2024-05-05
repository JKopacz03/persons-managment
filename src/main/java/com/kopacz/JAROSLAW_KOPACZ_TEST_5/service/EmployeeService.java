package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidWorkDateException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.NotExisitngUserWithPeselNumberException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.EmployeeEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.FindPersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PositionCommand;
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

    @Transactional
    @Override
    public void edit(String peselNumber, PersonEditCommand command) {
        EmployeeEditCommand updatedEmployee = modelMapper.map(command, EmployeeEditCommand.class);
        employeeRepository.findByPeselNumber(peselNumber)
                .map(employeeToEdit -> {
                    Optional.ofNullable(updatedEmployee.getFirstName()).ifPresent(employeeToEdit::setFirstName);
                    Optional.ofNullable(updatedEmployee.getLastName()).ifPresent(employeeToEdit::setLastName);
                    Optional.ofNullable(updatedEmployee.getPeselNumber()).ifPresent(employeeToEdit::setPeselNumber);
                    Optional.of(updatedEmployee.getHeight()).ifPresent(employeeToEdit::setHeight);
                    Optional.of(updatedEmployee.getWeight()).ifPresent(employeeToEdit::setWeight);
                    Optional.ofNullable(updatedEmployee.getEmail()).ifPresent(employeeToEdit::setEmail);
                    Optional.ofNullable(updatedEmployee.getVersion()).ifPresent(employeeToEdit::setVersion);
                    Optional.ofNullable(updatedEmployee.getSalary()).ifPresent(employeeToEdit::setSalary);
                    Optional.ofNullable(updatedEmployee.getActualProfession()).ifPresent(employeeToEdit::setActualProfession);
                    Optional.of(updatedEmployee.getNumberOfProfessions()).ifPresent(employeeToEdit::setNumberOfProfessions);

                    return employeeToEdit;
                }).orElseThrow(() -> new NotExisitngUserWithPeselNumberException("Bad pesel number"));
    }

    @Override
    @Transactional
    public List<EmployeeDto> findAll(
            FindPersonCommand findPersonCommand,
            Pageable pageable) {

        String firstName = findPersonCommand.getFirstName();
        String lastName = findPersonCommand.getLastName();
        String peselNumber = findPersonCommand.getPeselNumber();
        double heightFrom = findPersonCommand.getHeightFrom();
        double heightTo = findPersonCommand.getHeightTo();
        double weightFrom = findPersonCommand.getWeightFrom();
        double weightTo = findPersonCommand.getWeightTo();
        String email = findPersonCommand.getEmail();
        LocalDate workStartDateFrom = findPersonCommand.getWorkStartDateFrom();
        LocalDate workStartDateTo = findPersonCommand.getWorkStartDateTo();
        String actualProfession = findPersonCommand.getActualProfession();
        BigDecimal salaryFrom = findPersonCommand.getSalaryFrom();
        BigDecimal salaryTo = findPersonCommand.getSalaryTo();
        int numberOfProfessionsFrom = findPersonCommand.getNumberOfProfessionsFrom();
        int numberOfProfessionsTo = findPersonCommand.getNumberOfProfessionsTo();


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
        Employee employee = employeeRepository.findByPeselNumber(peselNumber).orElseThrow();
        if(employee.getPositions().stream()
                .anyMatch(e -> !command.getStartDate().isAfter(e.getEndDate())
                        && !command.getEndDate().isBefore(e.getStartDate()))){
            throw new InvalidWorkDateException("your dates are collide with another positions");
        }
        Position position = modelMapper.map(command, Position.class);
        position.setEmployee(employee);
        positionRepository.save(position);
    }

    public void imports(MultipartFile multipartFile) {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String appPath = new File("").getAbsolutePath();
            TEMP_STORAGE_ABSOLUTE = Paths.get(appPath, TEMP_STORAGE) + "/";

            File fileToImport = new File(TEMP_STORAGE_ABSOLUTE + originalFileName);
            multipartFile.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", TEMP_STORAGE_ABSOLUTE + originalFileName)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}


