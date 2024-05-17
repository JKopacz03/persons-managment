package com.kopacz.JAROSLAW_KOPACZ_TEST_5.controller;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.JobStatus;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.TypeCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JobStatusService;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final JobStatusService jobStatusService;
    private final ModelMapper modelMapper;

    @GetMapping("/find")
    public ResponseEntity<List<? extends PersonDto>> find(@Valid TypeCommand type,
                                                          @RequestParam Map<String, String> params,
                                                          @PageableDefault Pageable pageable) {
      return new ResponseEntity<>(personService.findAll(type, params, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PersonDto> add(@Valid @RequestBody PersonCommand command){
        return new ResponseEntity<>(personService.save(command), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> edit(@PathVariable("id") String id,
                                     @Valid @RequestBody PersonEditCommand command) {
        personService.edit(id, command);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'IMPORTER')")
    @PostMapping(value = "/import")
    public ResponseEntity<Long> importAll(@Valid TypeCommand type,
                                          @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(personService.imports(type, file), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'IMPORTER')")
    @GetMapping("/job-status/{id}")
    public ResponseEntity<JobStatus> jobsStatus(@PathVariable("id") Long id){
        return new ResponseEntity<>(jobStatusService.getJobStatus(id), HttpStatus.OK);
    }
}
