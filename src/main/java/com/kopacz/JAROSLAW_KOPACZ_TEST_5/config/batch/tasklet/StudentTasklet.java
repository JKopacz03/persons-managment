package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch.tasklet;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
@RequiredArgsConstructor
@Component
public class StudentTasklet implements Tasklet {

    private final ExecutionContext executionContext;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String fullPathFileName = (String) executionContext.get("fullPathFileName");

        File file = new File(fullPathFileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                try {
                    saveLineToDatabase(line);
                } catch (RuntimeException e){
                    throw new InvalidCsvException("Error reading file: " + fullPathFileName, e);
                }
            }
        } catch (IOException e) {
            throw new InvalidCsvException("Error reading file: " + fullPathFileName, e);
        }
        return RepeatStatus.FINISHED;
    }

    private void saveLineToDatabase(String line) {
        String[] fields = line.split(",");
        String sql = "INSERT INTO person (type, id, first_name, last_name, pesel_number, height, weight, email, version, college, academic_year, scholarship) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                fields[0],
                Long.parseLong(fields[1]),
                fields[2],
                fields[3],
                fields[4],
                Double.parseDouble(fields[5]),
                Double.parseDouble(fields[6]),
                fields[7],
                Integer.parseInt(fields[8]),
                fields[9],
                Integer.parseInt(fields[10]),
                Double.parseDouble(fields[11]));
    }

}

