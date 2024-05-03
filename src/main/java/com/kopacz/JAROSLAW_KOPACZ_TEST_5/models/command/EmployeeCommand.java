package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeCommand extends PersonCommand {
    @NotNull(message = "invalid date")
    private LocalDate workStartDate;
    @NotBlank(message = "actualProfession is mandatory")
    private String actualProfession;
    @DecimalMin(inclusive = false, value = "0.0", message = "missing salary")
    private BigDecimal salary;
    @Min(value = 0, message = "missing numberOfProfessions")
    private int numberOfProfessions;

    public EmployeeCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email, LocalDate workStartDate, String actualProfession, BigDecimal salary, int numberOfProfessions) {
        super(firstName, lastName, peselNumber, height, weight, email);
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
        this.numberOfProfessions = numberOfProfessions;
    }
}
