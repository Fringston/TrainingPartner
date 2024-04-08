package com.fredrikkodar.TrainingPartner.exceptions;

public class ExerciseNotFoundException extends RuntimeException{
    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
