package com.fredrikkodar.TrainingPartner.dto;

import lombok.Data;

@Data
public class MaxWeightDTO {

    private Long userId;
    private Long exerciseId;
    private int maxWeight;
    private String username;
    private String exerciseName;


}