package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCsvException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Pensioner;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.QPensioner;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PensionerDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonAddStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonImportStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PensionerRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class PensionerService implements PersonEditStrategy, PersonFindAllStrategy, PersonAddStrategy, PersonImportStrategy {
    private final PensionerRepository pensionerRepository;
    private final ModelMapper modelMapper;
    @Qualifier("runPensioner")
    private final Job job;
    private final JobLauncher jobLauncher;
    private String TEMP_STORAGE = "/src/main/resources/imports/";
    private String TEMP_STORAGE_ABSOLUTE;



    @Override
    public PersonDto save(PersonCommand personCommand) {
        Pensioner pensioner = modelMapper.map(personCommand, Pensioner.class);
        Pensioner savedPensioner = pensionerRepository.save(pensioner);
        return modelMapper.map(savedPensioner, PensionerDto.class);
    }

    @Transactional
    @Override
    public void edit(Long id, PersonEditCommand command) {
        Pensioner pensioner = modelMapper.map(command, Pensioner.class);
        pensionerRepository.saveAndFlush(pensioner);
    }


    @Override
    public List<PensionerDto> findAll(Map<String, String> params, Pageable pageable) {
        QPensioner pensioner = QPensioner.pensioner;
        BooleanExpression predicate = buildPredicate(params, pensioner);

        return pensionerRepository.findAll(predicate, pageable)
                .map(person -> modelMapper.map(person, PensionerDto.class))
                .toList();
    }

    private BooleanExpression buildPredicate(Map<String, String> params, QPensioner pensioner) {
        BooleanExpression predicate = pensioner.isNotNull();

        if (params.containsKey("firstName") && !params.get("firstName").isEmpty()) {
            predicate = predicate.and(pensioner.firstName.equalsIgnoreCase(params.get("firstName")));
        }
        if (params.containsKey("lastName") && !params.get("lastName").isEmpty()) {
            predicate = predicate.and(pensioner.lastName.equalsIgnoreCase(params.get("lastName")));
        }
        if (params.containsKey("peselNumber") && !params.get("peselNumber").isEmpty()) {
            predicate = predicate.and(pensioner.peselNumber.equalsIgnoreCase(params.get("peselNumber")));
        }
        if (params.containsKey("heightFrom") && !params.get("heightFrom").isEmpty()) {
            double heightFrom = Double.parseDouble(params.get("heightFrom"));
            predicate = predicate.and(pensioner.height.goe(heightFrom));
        }
        if (params.containsKey("heightTo") && !params.get("heightTo").isEmpty()) {
            double heightTo = Double.parseDouble(params.get("heightTo"));
            predicate = predicate.and(pensioner.height.loe(heightTo));
        }
        if (params.containsKey("weightFrom") && !params.get("weightFrom").isEmpty()) {
            double weightFrom = Double.parseDouble(params.get("weightFrom"));
            predicate = predicate.and(pensioner.weight.goe(weightFrom));
        }
        if (params.containsKey("weightTo") && !params.get("weightTo").isEmpty()) {
            double weightTo = Double.parseDouble(params.get("weightTo"));
            predicate = predicate.and(pensioner.weight.loe(weightTo));
        }
        if (params.containsKey("email") && !params.get("email").isEmpty()) {
            predicate = predicate.and(pensioner.email.equalsIgnoreCase(params.get("email")));
        }
        if (params.containsKey("pensionValueFrom") && !params.get("pensionValueFrom").isEmpty()) {
            BigDecimal pensionValueFrom = new BigDecimal(params.get("pensionValueFrom"));
            predicate = predicate.and(pensioner.pensionValue.goe(pensionValueFrom));
        }
        if (params.containsKey("pensionValueTo") && !params.get("pensionValueTo").isEmpty()) {
            BigDecimal workStartDateTo = new BigDecimal(params.get("pensionValueTo"));
            predicate = predicate.and(pensioner.pensionValue.loe(workStartDateTo));
        }
        if (params.containsKey("workYearsFrom") && !params.get("workYearsFrom").isEmpty()) {
            int workYearsFrom = Integer.parseInt(params.get("workYearsFrom"));
            predicate = predicate.and(pensioner.workYears.goe(workYearsFrom));
        }
        if (params.containsKey("workYearsTo") && !params.get("workYearsTo").isEmpty()) {
            int workYearsTo = Integer.parseInt(params.get("workYearsTo"));
            predicate = predicate.and(pensioner.workYears.loe(workYearsTo));
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


