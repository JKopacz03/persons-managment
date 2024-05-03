package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public abstract class PersonCommand {
    @NotBlank(message = "firstName is mandatory")
    private String firstName;
    @NotBlank(message = "lastName is mandatory")
    private String lastName;
    @NotBlank(message = "peselNumber is mandatory")
    private String peselNumber;
    @DecimalMin(inclusive = false, value = "0.0", message = "missing height")
    private double height;
    @DecimalMin(inclusive = false, value = "0.0", message = "missing weight")
    private double weight;
    @Email(message = "missing email")
    private String email;

    public PersonCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.peselNumber = peselNumber;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }
}
