package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find;

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
public class PersonFindCommand {
    private String firstName;
    private String lastName;
    private String peselNumber;
    private double heightFrom;
    private double heightTo;
    private double weightFrom;
    private double weightTo;
    private String email;

    public PersonFindCommand(String firstName, String lastName, String peselNumber, double heightFrom, double heightTo, double weightFrom, double weightTo, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.peselNumber = peselNumber;
        this.heightFrom = heightFrom;
        this.heightTo = heightTo;
        this.weightFrom = weightFrom;
        this.weightTo = weightTo;
        this.email = email;
    }
}
