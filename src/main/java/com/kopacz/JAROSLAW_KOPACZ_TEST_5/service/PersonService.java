package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.PersonFindCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonAddFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonEditFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonFindAllFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonAddFactory personAddFactory;
    private final PersonEditFactory personEditFactory;
    private final PersonFindAllFactory personFindAllFactory;

    public void save(PersonCommand command){
        Person person = personAddFactory.create(command);
        personRepository.save(person);
    }

    public List<? extends PersonDto> findAll(PersonFindCommand personFindCommand, Pageable pageable) {
        PersonFindAllStrategy strategy = personFindAllFactory.create(personFindCommand);
        return strategy.findAll(personFindCommand, pageable);
    }

    public void edit(String peselNumber, PersonEditCommand command) {
        PersonEditStrategy strategy = personEditFactory.create(command);
        strategy.edit(peselNumber, command);
    }
}
