package com.kopacz.JAROSLAW_KOPACZ_TEST_5.controller;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.JobStatus;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PositionCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.EmployeeService;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JobStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final JobStatusService jobStatusService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping("/{peselNumber}")
    public ResponseEntity<Void> addPosition(@PathVariable("peselNumber") String peselNumber,
                                            @Valid @RequestBody PositionCommand command){
        employeeService.addPosition(peselNumber, command);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'IMPORTER')")
    @PostMapping(value = "/import")
    public ResponseEntity<Long> importAll(@RequestPart("file") MultipartFile file) {
        Long id = employeeService.imports(file);
        return ResponseEntity.accepted().body(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'IMPORTER')")
    @GetMapping
    public ResponseEntity<JobStatus> jobsStatus(@RequestParam Long id){
        return ResponseEntity.ok(jobStatusService.getJobStatus(id));
    }
}
