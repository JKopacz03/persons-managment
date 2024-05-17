package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.JobStatusInvalidIdException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobStatusService {
    private final JobExplorer jobExplorer;

    public JobStatus getJobStatus(Long id) {
        JobExecution jobExecution = jobExplorer.getJobExecution(id);

        if(Objects.isNull(jobExecution)){
            throw new JobStatusInvalidIdException("Invalid id");
        }

        return new JobStatus(
                jobExecution.getStatus().toString(),
                jobExecution.getCreateTime(),
                jobExecution.getStartTime() != null ? jobExecution.getStartTime(): null,
                jobExecution.getStepExecutions().stream()
                        .mapToLong(StepExecution::getWriteCount)
                        .sum()
        );
    }
}