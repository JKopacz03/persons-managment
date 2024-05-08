package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.StringHelper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonEditFactory {
    @Autowired
    private final ApplicationContext applicationContext;
    private static final List<String> SERVICES_TYPES = new ArrayList<>();

    public PersonEditFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> PersonEditStrategy create(T command) {
        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("EditCommand", "");

        for (String service : SERVICES_TYPES) {
            if (service.startsWith(typeWithoutCommand)) {
                return (PersonEditStrategy) applicationContext.getBean(StringHelper.firstCharToLowerCase(service));
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    public static void add(String simpleName) {
        SERVICES_TYPES.add(simpleName);
    }
}
