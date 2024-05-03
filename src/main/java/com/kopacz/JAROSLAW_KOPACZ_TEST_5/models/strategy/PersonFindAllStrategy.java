package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.FindPersonCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonFindAllStrategy {
    List<? extends PersonDto> findAll(FindPersonCommand findPersonCommand, Pageable pageable);
}
