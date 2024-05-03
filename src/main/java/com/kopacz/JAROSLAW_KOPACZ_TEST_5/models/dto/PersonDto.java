package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDto {
    String firstName;
    String lastName;
    String peselNumber;
    double height;
    double weight;
    String email;
}
