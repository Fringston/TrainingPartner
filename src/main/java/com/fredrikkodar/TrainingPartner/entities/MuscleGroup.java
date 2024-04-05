package com.fredrikkodar.TrainingPartner.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "muscle_groups")
@Data
public class MuscleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long muscleGroupId;
    private String name;

    //En muskelgrupp kan användas i flera övningar, därför har den en en-till-många-relation med ExerciseEntity
    @OneToMany(mappedBy = "muscleGroup", cascade = CascadeType.ALL)
    List<Exercise> exercises = new ArrayList<>();

    public MuscleGroup(String name) {this.name = name;}

    public MuscleGroup() {}

    @Override
    public String toString() {
        return "MuscleGroup{" +
                "id=" + muscleGroupId +
                ", name='" + name + '\'' +
                '}';
    }
}
