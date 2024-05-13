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
@DiscriminatorValue("pensioner")
@Component
public class Pensioner extends Person implements Personable {
    private BigDecimal pensionValue;
    private int workYears;

    public Pensioner(UUID id, String firstName, String lastName, String peselNumber, double height, double weight, String email, String type, Integer version, BigDecimal pensionValue, int workYears) {
        super(id, firstName, lastName, peselNumber, height, weight, email, type, version);
        this.pensionValue = pensionValue;
        this.workYears = workYears;
    }
}
