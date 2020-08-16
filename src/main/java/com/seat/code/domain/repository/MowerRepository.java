package com.seat.code.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seat.code.domain.entity.MowerEntity;

public interface MowerRepository extends JpaRepository<MowerEntity, UUID> {

    Optional<MowerEntity> findOneByIdAndPlateauId(UUID mowerId, UUID plateauId);
}
