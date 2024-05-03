package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCommandException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy.PersonEditStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonEditFactory {
    @Autowired
    private ApplicationContext applicationContext;
    private final List<Class> servicesTypes = new ArrayList<>();

    public PersonEditFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        servicesTypes.addAll(findAllClasses("com.kopacz.JAROSLAW_KOPACZ_TEST_5.service"));
    }

    public <T> PersonEditStrategy create(T command) {

        String type = command.getClass().getSimpleName();
        String typeWithoutCommand = type.replace("EditCommand", "");

        for (Class<? extends PersonEditStrategy> clazz : servicesTypes) {
            if (clazz.getSimpleName().startsWith(typeWithoutCommand)) {
                return applicationContext.getBean(clazz);
            }
        }
        throw new InvalidCommandException("Invalid command type");
    }

    private Set<Class> findAllClasses(String packageName) {
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
