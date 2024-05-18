package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PersonEditCommand;

public interface PersonEditStrategy {
    void edit(Long id, PersonEditCommand command);
}
