package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StudentFindCommand extends PersonFindCommand {
    private String college;
    private int academicYearFrom;
    private int academicYearTo;
    private BigDecimal scholarshipFrom;
    private BigDecimal scholarshipTo;

    public StudentFindCommand(String firstName, String lastName, String peselNumber, double heightFrom, double heightTo, double weightFrom, double weightTo, String email, String college, int academicYearFrom, int academicYearTo, BigDecimal scholarshipFrom, BigDecimal scholarshipTo) {
        super(firstName, lastName, peselNumber, heightFrom, heightTo, weightFrom, weightTo, email);
        this.college = college;
        this.academicYearFrom = academicYearFrom;
        this.academicYearTo = academicYearTo;
        this.scholarshipFrom = scholarshipFrom;
        this.scholarshipTo = scholarshipTo;
    }
}
