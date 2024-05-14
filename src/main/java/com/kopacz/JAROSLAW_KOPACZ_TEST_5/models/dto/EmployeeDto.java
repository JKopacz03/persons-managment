package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto extends PersonDto {
    private LocalDate workStartDate;
    private String actualProfession;
    private BigDecimal salary;
    private int numberOfProfessions;
    private int numberOfPositions;

    public EmployeeDto(String firstName, String lastName, String peselNumber, double height, double weight, String email, LocalDate workStartDate, String actualProfession, BigDecimal salary, int numberOfProfessions, Set<Position> positions) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
        this.numberOfProfessions = numberOfProfessions;
        this.numberOfPositions = positions.size();
    }
}
