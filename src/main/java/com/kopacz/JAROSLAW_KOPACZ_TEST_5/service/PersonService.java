package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.FindPersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonAddFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonEditFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonFindAllFactory;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification.PersonSpecification.*;

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

    public List<? extends PersonDto> findAll(FindPersonCommand findPersonCommand, Pageable pageable) {
        PersonFindAllStrategy strategy = personFindAllFactory.create(findPersonCommand);
        return strategy.findAll(findPersonCommand, pageable);
    }

    public void edit(String peselNumber, PersonEditCommand command) {
        PersonEditStrategy strategy = personEditFactory.create(command);
        strategy.edit(peselNumber, command);
    }
}
