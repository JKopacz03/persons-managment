package com.kopacz.JAROSLAW_KOPACZ_TEST_5.controller;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.JobStatus;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JobStatusService;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.PensionerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pensioner")
public class PensionerController {
    private final PensionerService pensionerService;
    private final JobStatusService jobStatusService;

    @PreAuthorize("hasAnyRole('ADMIN', 'IMPORTER')")
    @PostMapping(value = "/import", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> importAll(@RequestPart("file") MultipartFile file) {
        pensionerService.imports(file);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'IMPORTER')")
    @GetMapping
    public ResponseEntity<JobStatus> jobsStatus(){
        return ResponseEntity.ok(jobStatusService.getJobStatus("importPensioners"));
    }
}
