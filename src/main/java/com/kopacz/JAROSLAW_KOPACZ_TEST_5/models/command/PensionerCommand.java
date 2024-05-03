package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PensionerCommand extends PersonCommand {
    @DecimalMin(inclusive = false, value = "0.0", message = "missing pensionValue")
    private BigDecimal pensionValue;
    @Min(value = 0, message = "missing workYears")
    private int workYears;

    public PensionerCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email, BigDecimal pensionValue, int workYears) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.pensionValue = pensionValue;
        this.workYears = workYears;
    }
}
