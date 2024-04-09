package com.fredrikkodar.TrainingPartner.dto;

import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import lombok.Data;

import java.util.Set;

@Data
public class ExerciseDTO {

    private Long exerciseId;
    private String name;
    private Set<MuscleGroup> muscleGroups;

}
