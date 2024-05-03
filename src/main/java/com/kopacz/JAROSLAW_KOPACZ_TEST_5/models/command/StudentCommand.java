package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCommand extends PersonCommand {
    @NotBlank(message = "college is mandatory")
    private String college;
    @Min(value = 0, message = "missing academicYear")
    private int academicYear;
    @DecimalMin(inclusive = false, value = "0.0", message = "missing scholarship")
    private BigDecimal scholarship;

    public StudentCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email, String college, int academicYear, BigDecimal scholarship) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.college = college;
        this.academicYear = academicYear;
        this.scholarship = scholarship;
    }
}
