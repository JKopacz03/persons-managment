package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StudentEditCommand extends PersonEditCommand {
    private String college;
    private int academicYear;
    private BigDecimal scholarship;

    public StudentEditCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email, String college, int academicYear, BigDecimal scholarship) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.college = college;
        this.academicYear = academicYear;
        this.scholarship = scholarship;
    }
}
