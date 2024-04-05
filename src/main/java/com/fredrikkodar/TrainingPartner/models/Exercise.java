package com.fredrikkodar.TrainingPartner.models;

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
