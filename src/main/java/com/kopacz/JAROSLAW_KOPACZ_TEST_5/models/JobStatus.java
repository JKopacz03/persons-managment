package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobStatus {
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private long processedCount;


}
