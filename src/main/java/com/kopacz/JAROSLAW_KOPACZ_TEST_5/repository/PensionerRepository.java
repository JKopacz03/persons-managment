package com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Pensioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PensionerRepository extends JpaRepository<Pensioner, UUID>, JpaSpecificationExecutor<Pensioner> {
    Optional<Pensioner> findByPeselNumber(String peselNumber);

}
