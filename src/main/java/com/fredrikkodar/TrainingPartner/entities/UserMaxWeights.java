package com.fredrikkodar.TrainingPartner.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Optional;

@Entity
@Table(name = "user_exercise_max")
@Data
public class UserMaxWeights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private int maxWeight;

    public UserMaxWeights() {}

    public UserMaxWeights(User user, Exercise exercise, int maxWeight) {
        this.user = user;
        this.exercise = exercise;
        this.maxWeight = maxWeight;
    }
}
