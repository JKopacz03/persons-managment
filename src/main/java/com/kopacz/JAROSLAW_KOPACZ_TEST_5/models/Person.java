package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DiscriminatorOptions;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
}
