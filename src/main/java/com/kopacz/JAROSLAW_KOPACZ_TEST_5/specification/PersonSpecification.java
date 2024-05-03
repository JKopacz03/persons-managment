package com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

    public static Specification<Person> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")), firstName.toLowerCase());
    }

    public static Specification<Person> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastName"), lastName);
    }

    public static Specification<Person> byPeselNumber(String peselNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("peselNumber"), peselNumber);
    }

    public static Specification<Person> byHeightFrom(double heightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("height"), heightFrom);
    }

    public static Specification<Person> byHeightTo(double heightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("height"), heightTo);
    }

    public static Specification<Person> byWeightFrom(double weightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), weightFrom);
    }

    public static Specification<Person> byWeightTo(double weightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("weight"), weightTo);
    }

    public static Specification<Person> byEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }
}

