package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
    @JsonManagedReference
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(workStartDate, employee.workStartDate) && Objects.equals(actualProfession, employee.actualProfession) && Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), workStartDate, actualProfession, salary);
    }
}
