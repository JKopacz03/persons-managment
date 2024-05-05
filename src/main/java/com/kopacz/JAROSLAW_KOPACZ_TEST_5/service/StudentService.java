package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCsvException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.NotExisitngUserWithPeselNumberException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.FindPersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.StudentEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.StudentDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.StudentRepository;
import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification.StudentSpecification.*;

@Service
public class StudentService implements PersonEditStrategy, PersonFindAllStrategy {
    private StudentRepository studentRepository;
    private ModelMapper modelMapper;
    private Job job;
    private JobLauncher jobLauncher;
    private String TEMP_STORAGE = "/src/main/resources/imports";
    private String TEMP_STORAGE_ABSOLUTE;
    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper, @Qualifier("runStudent") Job job, JobLauncher jobLauncher) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    @Transactional
    @Override
    public void edit(String peselNumber, PersonEditCommand command) {
        StudentEditCommand updatedStudent = modelMapper.map(command, StudentEditCommand.class);
        studentRepository.findByPeselNumber(peselNumber)
                .map(studentToEdit -> {
                    Optional.ofNullable(updatedStudent.getFirstName()).ifPresent(studentToEdit::setFirstName);
                    Optional.ofNullable(updatedStudent.getLastName()).ifPresent(studentToEdit::setLastName);
                    Optional.ofNullable(updatedStudent.getPeselNumber()).ifPresent(studentToEdit::setPeselNumber);
                    Optional.of(updatedStudent.getHeight()).ifPresent(studentToEdit::setHeight);
                    Optional.of(updatedStudent.getWeight()).ifPresent(studentToEdit::setWeight);
                    Optional.ofNullable(updatedStudent.getEmail()).ifPresent(studentToEdit::setEmail);
                    Optional.ofNullable(updatedStudent.getVersion()).ifPresent(studentToEdit::setVersion);
                    Optional.ofNullable(updatedStudent.getCollege()).ifPresent(studentToEdit::setCollege);
                    Optional.of(updatedStudent.getAcademicYear()).ifPresent(studentToEdit::setAcademicYear);
                    Optional.ofNullable(updatedStudent.getScholarship()).ifPresent(studentToEdit::setScholarship);

                    return studentToEdit;
                }).orElseThrow(() -> new NotExisitngUserWithPeselNumberException("Bad pesel number"));
    }
    @Override
    public List<StudentDto> findAll(FindPersonCommand findPersonCommand, Pageable pageable) {
        String firstName = findPersonCommand.getFirstName();
        String lastName = findPersonCommand.getLastName();
        String peselNumber = findPersonCommand.getPeselNumber();
        double heightFrom = findPersonCommand.getHeightFrom();
        double heightTo = findPersonCommand.getHeightTo();
        double weightFrom = findPersonCommand.getWeightFrom();
        double weightTo = findPersonCommand.getWeightTo();
        String email = findPersonCommand.getEmail();
        String college = findPersonCommand.getCollege();
        int academicYearFrom = findPersonCommand.getAcademicYearFrom();
        int academicYearTo = findPersonCommand.getAcademicYearTo();
        BigDecimal scholarshipFrom = findPersonCommand.getScholarshipFrom();
        BigDecimal scholarshipTo = findPersonCommand.getScholarshipTo();

        Specification<Student> filters = Specification.where(
                        StringUtils.isBlank(firstName) ? null : byFirstName(firstName))
                .and(StringUtils.isBlank(lastName) ? null : byLastName(lastName))
                .and(StringUtils.isBlank(peselNumber) ? null : byPeselNumber(peselNumber))
                .and(heightFrom > 0 ? byHeightFrom(heightFrom) : null)
                .and(heightTo > 0 ? byHeightTo(heightTo) : null)
                .and(weightFrom > 0 ? byWeightFrom(weightFrom) : null)
                .and(weightTo > 0 ? byWeightTo(weightTo) : null)
                .and(StringUtils.isBlank(email) ? null : byEmail(email))
                .and(StringUtils.isBlank(college) ? null : byCollege(college))
                .and(academicYearFrom > 0 ? byAcademicYearFrom(academicYearFrom) : null)
                .and(academicYearTo > 0 ? byAcademicYearTo(academicYearTo) : null)
                .and(Objects.isNull(scholarshipFrom) ? null : byScholarshipFrom(scholarshipFrom))
                .and(Objects.isNull(scholarshipTo) ? null : byScholarshipTo(scholarshipTo));


        return studentRepository.findAll(filters, pageable)
                .map(person -> modelMapper.map(person, StudentDto.class))
                .toList();
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
                 JobParametersInvalidException e) {

            throw new InvalidCsvException("Invalid csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
