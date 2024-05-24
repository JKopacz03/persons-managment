package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.QPerson;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.QStudent;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.TypeCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.StudentDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonAddStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonImportStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PersonRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonFactory personFactory;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public PersonDto save(PersonCommand command){
        PersonAddStrategy strategy = personFactory.createAddService(command);
        return strategy.save(command);
    }

    public List<? extends PersonDto> findAll(String type, Map<String, String> params, Pageable pageable) {
        if(Objects.isNull(type) || type.isBlank()){
            return findAll(params, pageable);
        }
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

    public List<PersonDto> findAll(Map<String, String> params, Pageable pageable) {
        QPerson person = QPerson.person;
        BooleanExpression predicate = buildPredicate(params, person);

        return personRepository.findAll(predicate, pageable)
                .map(p -> modelMapper.map(p, PersonDto.class))
                .toList();
    }

    private BooleanExpression buildPredicate(Map<String, String> params, QPerson person) {
        BooleanExpression predicate = person.isNotNull();

        if (params.containsKey("firstName") && !params.get("firstName").isEmpty()) {
            predicate = predicate.and(person.firstName.equalsIgnoreCase(params.get("firstName")));
        }
        if (params.containsKey("lastName") && !params.get("lastName").isEmpty()) {
            predicate = predicate.and(person.lastName.equalsIgnoreCase(params.get("lastName")));
        }
        if (params.containsKey("peselNumber") && !params.get("peselNumber").isEmpty()) {
            predicate = predicate.and(person.peselNumber.equalsIgnoreCase(params.get("peselNumber")));
        }
        if (params.containsKey("heightFrom") && !params.get("heightFrom").isEmpty()) {
            double heightFrom = Double.parseDouble(params.get("heightFrom"));
            predicate = predicate.and(person.height.goe(heightFrom));
        }
        if (params.containsKey("heightTo") && !params.get("heightTo").isEmpty()) {
            double heightTo = Double.parseDouble(params.get("heightTo"));
            predicate = predicate.and(person.height.loe(heightTo));
        }
        if (params.containsKey("weightFrom") && !params.get("weightFrom").isEmpty()) {
            double weightFrom = Double.parseDouble(params.get("weightFrom"));
            predicate = predicate.and(person.weight.goe(weightFrom));
        }
        if (params.containsKey("weightTo") && !params.get("weightTo").isEmpty()) {
            double weightTo = Double.parseDouble(params.get("weightTo"));
            predicate = predicate.and(person.weight.loe(weightTo));
        }
        if (params.containsKey("email") && !params.get("email").isEmpty()) {
            predicate = predicate.and(person.email.equalsIgnoreCase(params.get("email")));
        }

        return predicate;
    }
}
