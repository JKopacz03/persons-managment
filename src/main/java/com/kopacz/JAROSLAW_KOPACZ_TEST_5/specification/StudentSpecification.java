package com.kopacz.JAROSLAW_KOPACZ_TEST_5.specification;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class StudentSpecification {

    public static Specification<Student> byFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")), firstName.toLowerCase());
    }

    public static Specification<Student> byLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("lastName")), lastName.toLowerCase());
    }

    public static Specification<Student> byPeselNumber(String peselNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("peselNumber")), peselNumber.toLowerCase());
    }

    public static Specification<Student> byHeightFrom(double heightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("height"), heightFrom);
    }

    public static Specification<Student> byHeightTo(double heightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("height"), heightTo);
    }

    public static Specification<Student> byWeightFrom(double weightFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), weightFrom);
    }

    public static Specification<Student> byWeightTo(double weightTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("weight"), weightTo);
    }

    public static Specification<Student> byEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("email")), email.toLowerCase());
    }

    public static Specification<Student> byCollege(String college) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("college")), college.toLowerCase());
    }

    public static Specification<Student> byAcademicYearFrom(int academicYearFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("academicYear"), academicYearFrom);
    }

    public static Specification<Student> byAcademicYearTo(int academicYearTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("academicYear"), academicYearTo);
    }

    public static Specification<Student> byScholarshipFrom(BigDecimal scholarshipFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("scholarship"), scholarshipFrom);
    }

    public static Specification<Student> byScholarshipTo(BigDecimal scholarshipTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("scholarship"), scholarshipTo);
    }
}

