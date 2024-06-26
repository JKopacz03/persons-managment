package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("student")
public class Student extends Person {
    private String college;
    private int academicYear;
    private BigDecimal scholarship;

    public Student(Long id, String firstName, String lastName, String peselNumber, double height, double weight, String email, String type, Integer version, String college, int academicYear, BigDecimal scholarship) {
        super(id, firstName, lastName, peselNumber, height, weight, email, type, version);
        this.college = college;
        this.academicYear = academicYear;
        this.scholarship = scholarship;
    }
}
