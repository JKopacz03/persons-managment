package com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification;


import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeSpecification {

    public static Specification<Employee> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")), firstName.toLowerCase());
    }

    public static Specification<Employee> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("lastName")), lastName.toLowerCase());
    }

    public static Specification<Employee> byPeselNumber(String peselNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("peselNumber")), peselNumber.toLowerCase());
    }

    public static Specification<Employee> byHeightFrom(double heightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("height"), heightFrom);
    }

    public static Specification<Employee> byHeightTo(double heightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("height"), heightTo);
    }

    public static Specification<Employee> byWeightFrom(double weightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), weightFrom);
    }

    public static Specification<Employee> byWeightTo(double weightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("weight"), weightTo);
    }

    public static Specification<Employee> byEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("email")), email.toLowerCase());
    }

    public static Specification<Employee> byWorkStartDateFrom(LocalDate workStartDateFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("workStartDate"), workStartDateFrom);
    }

    public static Specification<Employee> byWorkStartDateTo(LocalDate workStartDateTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("workStartDate"), workStartDateTo);
    }

    public static Specification<Employee> byActualProfession(String actualProfession) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("actualProfession")), actualProfession.toLowerCase());
    }

    public static Specification<Employee> bySalaryFrom(BigDecimal salaryFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), salaryFrom);
    }

    public static Specification<Employee> bySalaryTo(BigDecimal salaryTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("salary"), salaryTo);
    }

    public static Specification<Employee> byNumberOfProfessionsFrom(int numberOfProfessionsFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfProfessions"), numberOfProfessionsFrom);
    }

    public static Specification<Employee> byNumberOfProfessionsTo(int numberOfProfessionsTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("numberOfProfessions"), numberOfProfessionsTo);
    }

}


