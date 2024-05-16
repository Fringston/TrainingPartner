package com.fredrikkodar.TrainingPartner.dto;

import lombok.Data;

import java.util.List;

@Data
public class MuscleGroupDTO {

    private Long muscleGroupId;
    private String name;
    private List<ExerciseDTO> exercises;
}
