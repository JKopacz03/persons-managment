package com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Person;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, Long>, QuerydslPredicateExecutor<Person> {
}
