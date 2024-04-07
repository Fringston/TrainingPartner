package com.fredrikkodar.TrainingPartner.repository;

import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeights;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMaxWeightsRepository extends JpaRepository<UserMaxWeights, Long>{
    Optional<UserMaxWeights> findByUser_UserIdAndExercise_ExerciseId(Long userId, Long exerciseId);
}
