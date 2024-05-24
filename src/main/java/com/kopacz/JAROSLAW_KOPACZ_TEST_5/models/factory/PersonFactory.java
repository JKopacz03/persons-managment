package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.TypeCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonAddStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonFindAllStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonImportStrategy;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PersonFactory {
    private final List<PersonEditStrategy> editServicesTypes;
    private final List<PersonFindAllStrategy> findServicesTypes;
    private final List<PersonAddStrategy> addServicesTypes;
    private final List<PersonImportStrategy> importServicesTypes;
    public <T> PersonAddStrategy createAddService(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("Command", "");

        for (PersonAddStrategy service : addServicesTypes) {
            if (service.getClass().getSimpleName().startsWith(typeWithoutCommand)) {
                return service;
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    public <T> PersonEditStrategy createEditService(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("EditCommand", "");

        for (PersonEditStrategy service : editServicesTypes) {
            if (service.getClass().getSimpleName().startsWith(typeWithoutCommand)) {
                return service;
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    public PersonFindAllStrategy createFindService(String type) {
        for (PersonFindAllStrategy service : findServicesTypes) {
            if (service.getClass().getSimpleName().toLowerCase()
                    .startsWith(type.toLowerCase())) {
                return service;
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    public PersonImportStrategy createImportService(TypeCommand type) {
        for (PersonImportStrategy service : importServicesTypes) {
            if (service.getClass().getSimpleName().toLowerCase()
                    .startsWith(type.getType().toLowerCase())) {
                return service;
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }
}
