package com.fredrikkodar.TrainingPartner.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "exercises")
@Data
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "exercise_muscle_group_junction",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_group_id")
    )
    private Set<MuscleGroup> muscleGroups;

    public Exercise(String name) {
        this.name = name;
        this.muscleGroups = new HashSet<>();
    }

    public Exercise() {
        this.muscleGroups = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + exerciseId +
                ", name='" + name + '\'' +
                ", muscleGroups=" + (muscleGroups != null ? muscleGroups.stream().map(MuscleGroup::getName).collect(Collectors.joining(", ")) : "None") +
                '}';
    }
}
