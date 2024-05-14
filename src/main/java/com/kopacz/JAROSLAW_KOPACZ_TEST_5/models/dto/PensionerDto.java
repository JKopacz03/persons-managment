package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PensionerDto extends PersonDto {
    private BigDecimal pensionValue;
    private int workYears;
}
