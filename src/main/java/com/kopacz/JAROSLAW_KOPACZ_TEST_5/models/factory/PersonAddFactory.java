package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.EmployeeCommand;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PersonAddFactory {

    private static final Map<String, Class<? extends Person>> PERSON_TYPES = new HashMap<>();

    @Autowired
    private final ModelMapper modelMapper;

    public PersonAddFactory(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T> Person create(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("Command", "");

        for (String personType : PERSON_TYPES.keySet()) {
            if (personType.startsWith(typeWithoutCommand)) {
                return modelMapper.map(command, PERSON_TYPES.get(personType));
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    public static void add(String personType, Class<? extends Person> personClass) {
        PERSON_TYPES.put(personType, personClass);
    }
}

