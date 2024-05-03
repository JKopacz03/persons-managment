package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import org.springframework.stereotype.Component;

public interface PersonEditStrategy {
    void edit(String peselNumber, PersonEditCommand command);
}
