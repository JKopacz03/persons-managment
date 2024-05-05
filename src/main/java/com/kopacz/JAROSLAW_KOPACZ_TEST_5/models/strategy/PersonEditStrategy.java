package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PersonEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.StudentEditCommand;
import org.springframework.transaction.annotation.Transactional;

public interface PersonEditStrategy {
    void edit(String peselNumber, PersonEditCommand command);
}
