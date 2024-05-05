package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DiscriminatorOptions;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorOptions(force = true)
@EqualsAndHashCode(of = "id")
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String peselNumber;
    private double height;
    private double weight;
    private String email;
    @Column(insertable = false, updatable = false)
    private String type;
    @Version
    private Integer version;

    public void setVersion(Integer version) {
        if (!Objects.equals(this.version, version)) {
            throw new OptimisticLockingFailureException("invalid version");
        }
    }
}
