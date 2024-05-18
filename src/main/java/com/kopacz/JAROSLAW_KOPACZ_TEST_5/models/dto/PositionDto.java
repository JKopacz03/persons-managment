package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PositionDto {
    private Long positionId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salary;
    private Long employee;

    public void setEmployee(Employee employee) {
        this.employee = employee.getId();
    }
}
