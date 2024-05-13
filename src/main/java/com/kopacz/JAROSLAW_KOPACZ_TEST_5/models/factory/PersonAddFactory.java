package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Personable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PersonAddFactory {

    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final List<Person> types;

    public PersonAddFactory(ModelMapper modelMapper, List<Person> types) {
        this.modelMapper = modelMapper;
        this.types = types;
    }

    public <T> Person create(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("Command", "");

        for (Person personType : types) {
            if (personType.getClass().getSimpleName().startsWith(typeWithoutCommand)) {
                return modelMapper.map(command, personType.getClass());
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }
}

