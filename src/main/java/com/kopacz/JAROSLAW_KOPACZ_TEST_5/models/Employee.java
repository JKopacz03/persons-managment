package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.json.PositionsSerializer;
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
public class Employee extends Person {
    private LocalDate workStartDate;
    private String actualProfession;
    private BigDecimal salary;
    @OneToMany(
            mappedBy = "employee"
    )
    @JsonSerialize(using = PositionsSerializer.class)
    private Set<Position> positions;

    @Override
    public String toString() {
        return "Employee{" +
                "workStartDate=" + workStartDate +
                ", actualProfession='" + actualProfession + '\'' +
                ", salary=" + salary +
                ", positions=" + positions.size() +
                '}';
    }
    public Employee(Long id, String firstName, String lastName, String peselNumber, double height, double weight, String email, String type, Integer version, LocalDate workStartDate, String actualProfession, BigDecimal salary) {
        super(id, firstName, lastName, peselNumber, height, weight, email, type, version);
        this.workStartDate = workStartDate;
        this.actualProfession = actualProfession;
        this.salary = salary;
        this.positions = new HashSet<>();
    }
}
