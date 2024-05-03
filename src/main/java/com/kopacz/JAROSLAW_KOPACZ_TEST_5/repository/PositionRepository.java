package com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PositionRepository extends JpaRepository<Position, UUID> {
}
