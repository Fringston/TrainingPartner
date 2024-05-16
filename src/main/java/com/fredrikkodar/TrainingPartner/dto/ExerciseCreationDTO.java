package com.fredrikkodar.TrainingPartner.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ExerciseCreationDTO {

    private Long exerciseId;
    private String name;
    private Set<Long> muscleGroupIds;
    private boolean hasMaxWeight;

}
