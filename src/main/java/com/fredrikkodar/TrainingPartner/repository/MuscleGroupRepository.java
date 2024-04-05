package com.fredrikkodar.TrainingPartner.repository;

import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Long> {
    boolean existsByName(String name);
    Optional<MuscleGroup> findByName(String name);
}