package com.fredrikkodar.TrainingPartner.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exercises")
@Data
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;
    private String name;

    @ManyToOne
    @JoinColumn(name = "muscle_group_id")
    private MuscleGroup muscleGroup;

    public Exercise(String name) {
        this.name = name;
    }
    public Exercise() {}

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + exerciseId +
                ", name='" + name + '\'' +
                '}';
    }
}
