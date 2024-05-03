package com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByPeselNumber(String peselNumber);
}
