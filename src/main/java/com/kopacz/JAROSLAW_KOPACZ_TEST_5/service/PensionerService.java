package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.NotExisitngUserWithPeselNumberException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Pensioner;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.FindPersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PensionerEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PensionerDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PensionerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
import java.util.*;

import static com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification.PensionerSpecification.*;

@Service
public class PensionerService implements PersonEditStrategy, PersonFindAllStrategy {
    private PensionerRepository pensionerRepository;
    private ModelMapper modelMapper;
    private Job job;
    private JobLauncher jobLauncher;
    private String TEMP_STORAGE = "/src/main/resources/imports/";
    private String TEMP_STORAGE_ABSOLUTE;

    public PensionerService(PensionerRepository pensionerRepository, ModelMapper modelMapper, @Qualifier("runPensioner") Job job, JobLauncher jobLauncher) {
        this.pensionerRepository = pensionerRepository;
        this.modelMapper = modelMapper;
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    @Transactional
    @Override
    public void edit(String peselNumber, PersonEditCommand command) {
        PensionerEditCommand updatedPensioner = modelMapper.map(command, PensionerEditCommand.class);
        pensionerRepository.findByPeselNumber(peselNumber)
                .map(pensionerToEdit -> {
                    Optional.ofNullable(updatedPensioner.getFirstName()).ifPresent(pensionerToEdit::setFirstName);
                    Optional.ofNullable(updatedPensioner.getLastName()).ifPresent(pensionerToEdit::setLastName);
                    Optional.ofNullable(updatedPensioner.getPeselNumber()).ifPresent(pensionerToEdit::setPeselNumber);
                    Optional.of(updatedPensioner.getHeight()).ifPresent(pensionerToEdit::setHeight);
                    Optional.of(updatedPensioner.getWeight()).ifPresent(pensionerToEdit::setWeight);
                    Optional.ofNullable(updatedPensioner.getEmail()).ifPresent(pensionerToEdit::setEmail);
                    Optional.ofNullable(updatedPensioner.getVersion()).ifPresent(pensionerToEdit::setVersion);
                    Optional.ofNullable(updatedPensioner.getPensionValue()).ifPresent(pensionerToEdit::setPensionValue);
                    Optional.of(updatedPensioner.getWorkYears()).ifPresent(pensionerToEdit::setWorkYears);

                    return pensionerToEdit;
                }).orElseThrow(() -> new NotExisitngUserWithPeselNumberException("Bad pesel number"));
    }


    @Override
    public List<PensionerDto> findAll(FindPersonCommand findPersonCommand, Pageable pageable) {
        String firstName = findPersonCommand.getFirstName();
        String lastName = findPersonCommand.getLastName();
        String peselNumber = findPersonCommand.getPeselNumber();
        double heightFrom = findPersonCommand.getHeightFrom();
        double heightTo = findPersonCommand.getHeightTo();
        double weightFrom = findPersonCommand.getWeightFrom();
        double weightTo = findPersonCommand.getWeightTo();
        String email = findPersonCommand.getEmail();
        BigDecimal pensionValueFrom = findPersonCommand.getPensionValueFrom();
        BigDecimal pensionValueTo = findPersonCommand.getPensionValueTo();
        int workYearsFrom = findPersonCommand.getWorkYearsFrom();
        int workYearsTo = findPersonCommand.getWorkYearsTo();


        Specification<Pensioner> filters = Specification.where(
                        StringUtils.isBlank(firstName) ? null : byFirstName(firstName))
                .and(StringUtils.isBlank(lastName) ? null : byLastName(lastName))
                .and(StringUtils.isBlank(peselNumber) ? null : byPeselNumber(peselNumber))
                .and(heightFrom > 0 ? byHeightFrom(heightFrom) : null)
                .and(heightTo > 0 ? byHeightTo(heightTo) : null)
                .and(weightFrom > 0 ? byWeightFrom(weightFrom) : null)
                .and(weightTo > 0 ? byWeightTo(weightTo) : null)
                .and(StringUtils.isBlank(email) ? null : byEmail(email))
                .and(Objects.isNull(pensionValueFrom) ? null : byPensionValueFrom(pensionValueFrom))
                .and(Objects.isNull(pensionValueTo) ? null : byPensionValueTo(pensionValueTo))
                .and(workYearsFrom > 0 ? byWorkYearFrom(workYearsFrom) : null)
                .and(workYearsTo > 0 ? byWorkYearTo(workYearsTo) : null);

            return pensionerRepository.findAll(filters, pageable)
                    .map(person -> modelMapper.map(person, PensionerDto.class))
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

            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


