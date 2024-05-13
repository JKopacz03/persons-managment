package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonAddFactory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DiscriminatorOptions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("student")
@Component
public class Student extends Person implements Personable {
    private String college;
    private int academicYear;
    private BigDecimal scholarship;

    public Student(UUID id, String firstName, String lastName, String peselNumber, double height, double weight, String email, String type, Integer version, String college, int academicYear, BigDecimal scholarship) {
        super(id, firstName, lastName, peselNumber, height, weight, email, type, version);
        this.college = college;
        this.academicYear = academicYear;
        this.scholarship = scholarship;
    }
}
