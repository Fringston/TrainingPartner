package com.fredrikkodar.TrainingPartner.repository;

import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMaxWeightRepository extends JpaRepository<UserMaxWeight, Long>{
    Optional<UserMaxWeight> findByUser_UserIdAndExercise_ExerciseId(Long userId, Long exerciseId);
    void deleteByUser_UserIdAndExercise_ExerciseId(Long userId, Long exerciseId);
}
