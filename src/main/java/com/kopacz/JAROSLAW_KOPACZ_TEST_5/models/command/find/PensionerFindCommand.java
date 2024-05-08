package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PensionerFindCommand extends PersonFindCommand {
    private BigDecimal pensionValueFrom;
    private BigDecimal pensionValueTo;
    private int workYearsFrom;
    private int workYearsTo;

    public PensionerFindCommand(String firstName, String lastName, String peselNumber, double heightFrom, double heightTo, double weightFrom, double weightTo, String email, BigDecimal pensionValueFrom, BigDecimal pensionValueTo, int workYearsFrom, int workYearsTo) {
        super(firstName, lastName, peselNumber, heightFrom, heightTo, weightFrom, weightTo, email);
        this.pensionValueFrom = pensionValueFrom;
        this.pensionValueTo = pensionValueTo;
        this.workYearsFrom = workYearsFrom;
        this.workYearsTo = workYearsTo;
    }
}
