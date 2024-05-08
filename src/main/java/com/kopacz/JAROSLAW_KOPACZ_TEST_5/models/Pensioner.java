package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.factory.PersonAddFactory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DiscriminatorOptions;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("pensioner")
public class Pensioner extends Person {
    private BigDecimal pensionValue;
    private int workYears;

    static {
        PersonAddFactory.add("Pensioner", Pensioner.class);
    }

    public Pensioner(UUID id, String firstName, String lastName, String peselNumber, double height, double weight, String email, String type, Integer version, BigDecimal pensionValue, int workYears) {
        super(id, firstName, lastName, peselNumber, height, weight, email, type, version);
        this.pensionValue = pensionValue;
        this.workYears = workYears;
    }
}
