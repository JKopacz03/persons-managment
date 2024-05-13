package com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT e FROM Employee e WHERE e.peselNumber = :peselNumber")
    Optional<Employee> findByPeselNumberWithPessimisticLock(@Param("peselNumber") String peselNumber);

    Optional<Employee> findByPeselNumber(String peselNumber);
}
