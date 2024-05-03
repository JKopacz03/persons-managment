package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PositionCommand {
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotNull(message = "startDate is mandatory")
    private LocalDate startDate;
    @NotNull(message = "endDate is mandatory")
    private LocalDate endDate;
    @DecimalMin(value = "0.0", message = "invalid salary")
    private BigDecimal salary;
}
