package com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByPeselNumber(String peselNumber);
}
