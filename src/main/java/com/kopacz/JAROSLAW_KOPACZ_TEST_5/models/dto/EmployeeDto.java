package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class EmployeeDto extends PersonDto {
    private UUID employeeId;
    private LocalDate workStartDate;
    private String actualProfession;
    private BigDecimal salary;
    private int numberOfPositions;

    public EmployeeDto(UUID id, String firstName, String lastName, String peselNumber, double height, double weight, String email, LocalDate workStartDate, String actualProfession, BigDecimal salary, int numberOfPositions) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.employeeId = id;
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
        this.numberOfPositions = numberOfPositions;
    }
}
