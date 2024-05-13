package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonEditFactory {
    @Autowired
    private final List<PersonEditStrategy> servicesTypes;

    public PersonEditFactory(List<PersonEditStrategy> servicesTypes) {
        this.servicesTypes = servicesTypes;
    }

    public <T> PersonEditStrategy create(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("EditCommand", "");

        for (PersonEditStrategy service : servicesTypes) {
            if (service.getClass().getSimpleName().startsWith(typeWithoutCommand)) {
                return service;
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }
}
