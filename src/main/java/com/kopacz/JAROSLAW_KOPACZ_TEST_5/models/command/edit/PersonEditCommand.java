package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public abstract class PersonEditCommand {
    @Min(value = 1, message = "id is mandatory")
    private Long id;
    private String firstName;
    private String lastName;
    private String peselNumber;
    private double height;
    private double weight;
    private String email;
    private Integer version;

    public PersonEditCommand(Long id, String firstName, String lastName, String peselNumber, double height, double weight, String email, Integer version) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.peselNumber = peselNumber;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.version = version;
    }
}
