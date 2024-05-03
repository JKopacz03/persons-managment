package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobStatusService {
    private final JobExplorer jobExplorer;

    public JobStatus getJobStatus(String jobName) {
        JobInstance jobInstance = jobExplorer.getLastJobInstance(jobName);
        JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);

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