package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.PersonDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PersonFindAllStrategy {
    List<? extends PersonDto> findAll(Map<String, String> params, Pageable pageable);
}
