package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.TypeCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonAddStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonImportStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonFactory personFactory;

    public PersonDto save(PersonCommand command){
        PersonAddStrategy strategy = personFactory.createAddService(command);
        return strategy.save(command);
    }

    public List<? extends PersonDto> findAll(TypeCommand type, Map<String, String> params, Pageable pageable) {
        PersonFindAllStrategy strategy = personFactory.createFindService(type);
        return strategy.findAll(params, pageable);
    }

    public void edit(Long peselNumber, PersonEditCommand command) {
        PersonEditStrategy strategy = personFactory.createEditService(command);
        strategy.edit(peselNumber, command);
    }

    public Long imports(TypeCommand type, MultipartFile file) {
        PersonImportStrategy strategy = personFactory.createImportService(type);
        return strategy.imports(file);
    }
}
