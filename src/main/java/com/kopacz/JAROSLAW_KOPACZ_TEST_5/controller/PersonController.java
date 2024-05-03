package com.kopacz.JAROSLAW_KOPACZ_TEST_5.controller;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.FindPersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    @GetMapping("/find")
    public ResponseEntity<List<? extends PersonDto>> find(@Valid FindPersonCommand findPersonCommand,
                                                          @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(personService.findAll(findPersonCommand, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody PersonCommand command){
        personService.save(command);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{peselNumber}")
    public ResponseEntity<Void> edit(@PathVariable("peselNumber") String peselNumber,
                                     @Valid @RequestBody PersonEditCommand command) {
        personService.edit(peselNumber, command);
        return ResponseEntity.ok().build();
    }
}
