package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EmployeeEditCommand extends PersonEditCommand {
    private LocalDate workStartDate;
    private String actualProfession;
    private BigDecimal salary;
    private int numberOfProfessions;

    public EmployeeEditCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email, LocalDate workStartDate, String actualProfession, BigDecimal salary, int numberOfProfessions) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
        this.numberOfProfessions = numberOfProfessions;
    }
}
