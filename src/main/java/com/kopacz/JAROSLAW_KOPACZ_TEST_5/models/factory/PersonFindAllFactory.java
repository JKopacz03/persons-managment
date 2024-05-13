package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonFindAllFactory {
    @Autowired
    private final List<PersonFindAllStrategy> servicesTypes;

    public PersonFindAllFactory(List<PersonFindAllStrategy> servicesTypes) {
        this.servicesTypes = servicesTypes;
    }

    public <T> PersonFindAllStrategy create(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("FindCommand", "");

        for (PersonFindAllStrategy service : servicesTypes) {
            if (service.getClass().getSimpleName().startsWith(typeWithoutCommand)) {
                return service;
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }
}
