package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("employee")
@Component
public class Employee extends Person implements Personable {
    private LocalDate workStartDate;
    private String actualProfession;
    private BigDecimal salary;
    private int numberOfProfessions;
    @OneToMany(
            mappedBy = "employee"
    )
    private Set<Position> positions;

    public Employee(UUID id, String firstName, String lastName, String peselNumber, double height, double weight, String email, String type, Integer version, LocalDate workStartDate, String actualProfession, BigDecimal salary, int numberOfProfessions) {
        super(id, firstName, lastName, peselNumber, height, weight, email, type, version);
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
        this.numberOfProfessions = numberOfProfessions;
        this.positions = new HashSet<>();
    }

}
