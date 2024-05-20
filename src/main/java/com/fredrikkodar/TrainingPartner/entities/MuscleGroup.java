package com.fredrikkodar.TrainingPartner.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "muscle_groups")
@Data
public class MuscleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long muscleGroupId;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "muscleGroups")
    private Set<Exercise> exercises;

    public MuscleGroup(String name) {
        this.name = name;
        this.exercises = new HashSet<>();
    }

    public MuscleGroup() {
        this.exercises = new HashSet<>();
    }

    //public MuscleGroup(String name) {this.name = name;}

    //public MuscleGroup() {}

    @Override
    public String toString() {
        return "MuscleGroup{" +
                "id=" + muscleGroupId +
                ", name='" + name + '\'' +
                ", exercises=" + (exercises != null ? exercises.stream().map(Exercise::getName).collect(Collectors.joining(", ")) : "None") +
                '}';
    }
}
