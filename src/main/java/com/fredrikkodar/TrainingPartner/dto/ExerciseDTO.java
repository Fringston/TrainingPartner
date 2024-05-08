package com.fredrikkodar.TrainingPartner.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ExerciseDTO {

    private Long exerciseId;
    private String name;
    private Set<Long> muscleGroupId;
    private boolean possibleMaxWeight;
    private String suggestedWeight;
    private String setsAndReps;

    public void generalSuggestedWeight() {
        this.suggestedWeight = "Choose a weight that allows you to complete the desired number of sets and reps with good form.";
    }
}
