package com.fredrikkodar.TrainingPartner.repository;

import com.fredrikkodar.TrainingPartner.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByName(String name);
    List<Exercise> findByMuscleGroups_MuscleGroupId(Long muscleGroupId);
}
