package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EmployeeFindCommand extends PersonFindCommand {
    private LocalDate workStartDateFrom;
    private LocalDate workStartDateTo;
    private String actualProfession;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private int numberOfProfessionsFrom;
    private int numberOfProfessionsTo;

    public EmployeeFindCommand(String firstName, String lastName, String peselNumber, double heightFrom, double heightTo, double weightFrom, double weightTo, String email, LocalDate workStartDateFrom, LocalDate workStartDateTo, String actualProfession, BigDecimal salaryFrom, BigDecimal salaryTo, int numberOfProfessionsFrom, int numberOfProfessionsTo) {
        super(firstName, lastName, peselNumber, heightFrom, heightTo, weightFrom, weightTo, email);
        this.workStartDateFrom = workStartDateFrom;
        this.workStartDateTo = workStartDateTo;
        this.actualProfession = actualProfession;
        this.salaryFrom = salaryFrom;
        this.salaryTo = salaryTo;
        this.numberOfProfessionsFrom = numberOfProfessionsFrom;
        this.numberOfProfessionsTo = numberOfProfessionsTo;
    }
}
