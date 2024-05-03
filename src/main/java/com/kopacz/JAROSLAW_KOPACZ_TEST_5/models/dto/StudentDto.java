package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto extends PersonDto {
    private String college;
    private int academicYear;
    private BigDecimal scholarship;
}
