package com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Pensioner;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PensionerSpecification {

    public static Specification<Pensioner> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")), firstName.toLowerCase());
    }

    public static Specification<Pensioner> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("lastName")), lastName.toLowerCase());
    }

    public static Specification<Pensioner> byPeselNumber(String peselNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("peselNumber")), peselNumber.toLowerCase());
    }

    public static Specification<Pensioner> byHeightFrom(double heightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("height"), heightFrom);
    }

    public static Specification<Pensioner> byHeightTo(double heightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("height"), heightTo);
    }

    public static Specification<Pensioner> byWeightFrom(double weightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), weightFrom);
    }

    public static Specification<Pensioner> byWeightTo(double weightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("weight"), weightTo);
    }

    public static Specification<Pensioner> byEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("email")), email.toLowerCase());
    }

    public static Specification<Pensioner> byWorkYearFrom(int workYearFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("workYears"), workYearFrom);
    }

    public static Specification<Pensioner> byWorkYearTo(int workYearTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("workYears"), workYearTo);
    }

    public static Specification<Pensioner> byPensionValueFrom(BigDecimal pensionValueFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("pensionValue"), pensionValueFrom);
    }

    public static Specification<Pensioner> byPensionValueTo(BigDecimal pensionValueTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("pensionValue"), pensionValueTo);
    }

}


