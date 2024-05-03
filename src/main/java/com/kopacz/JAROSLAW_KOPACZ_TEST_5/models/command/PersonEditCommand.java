package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    private String firstName;
    private String lastName;
    private String peselNumber;
    private double height;
    private double weight;
    private String email;

    public PersonEditCommand(String firstName, String lastName, String peselNumber, double height, double weight, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.peselNumber = peselNumber;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }
}
