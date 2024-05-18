package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit;

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

    public EmployeeEditCommand(Long id, String firstName, String lastName, String peselNumber, double height, double weight, String email, Integer version, LocalDate workStartDate, String actualProfession, BigDecimal salary) {
        super(id, firstName, lastName, peselNumber, height, weight, email, version);
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
    }
}
