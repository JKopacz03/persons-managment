package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PensionerEditCommand extends PersonEditCommand {

    private BigDecimal pensionValue;
    private int workYears;

    public PensionerEditCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email, Integer version, BigDecimal pensionValue, int workYears) {
        super(firstName, lastName, peselNumber, height, weight, email, version);
        this.pensionValue = pensionValue;
        this.workYears = workYears;
    }
}
