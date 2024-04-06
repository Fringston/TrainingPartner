package com.fredrikkodar.TrainingPartner.dto;

import lombok.Data;

@Data
public class MaxWeightRequestDTO {

    private Long userId;
    private Long exerciseId;
    private int maxWeight;


}