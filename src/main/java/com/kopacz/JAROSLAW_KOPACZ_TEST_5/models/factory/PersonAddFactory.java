package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.EmployeeCommand;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonAddFactory {

    private final List<Class> personTypes = new ArrayList<>();
    @Autowired
    private final ModelMapper modelMapper;

    public PersonAddFactory(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        personTypes.addAll(findAllClasses("com.kopacz.JAROSLAW_KOPACZ_TEST_5.models"));
    }

    public <T> Person create(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("Command", "");

        for (Class<? extends Person> clazz : personTypes) {
            if (clazz.getSimpleName().startsWith(typeWithoutCommand)) {
                return modelMapper.map(command, clazz);
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    public Set<Class> findAllClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            throw new InvalidCommandException("Invalid command type");
        }
    }

}

