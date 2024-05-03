package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPersonCommand {
    private String firstName;
    private String lastName;
    private String peselNumber;
    private double heightFrom;
    private double heightTo;
    private double weightFrom;
    private double weightTo;
    private String email;
    private BigDecimal pensionValueFrom;
    private BigDecimal pensionValueTo;
    private int workYearsFrom;
    private int workYearsTo;
    private String college;
    private int academicYearFrom;
    private int academicYearTo;
    private BigDecimal scholarshipFrom;
    private BigDecimal scholarshipTo;
    private LocalDate workStartDateFrom;
    private LocalDate workStartDateTo;
    private String actualProfession;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private int numberOfProfessionsFrom;
    private int numberOfProfessionsTo;
    @NotBlank(message = "missing type")
    private String type;
}
