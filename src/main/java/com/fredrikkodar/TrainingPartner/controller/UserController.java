package com.fredrikkodar.TrainingPartner.controller;

import com.fredrikkodar.TrainingPartner.dto.*;
import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.exceptions.*;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Tag(name = "User", description = "User API")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        try {
            UserDTO user = userService.getUser(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDTO request) {
        try {
            userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/maxweight/{userId}/{exerciseId}")
    public ResponseEntity<MaxWeightDTO> getMaxWeight(@PathVariable Long userId, @PathVariable Long exerciseId) {
        try {
            MaxWeightDTO dto = userService.getMaxWeight(userId, exerciseId);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException | ExerciseNotFoundException | MaxWeightNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/maxweights/{userId}")
    public ResponseEntity<List<MaxWeightDTO>> getAllMaxWeights(@PathVariable Long userId) {
        try {
            List<MaxWeightDTO> dtos = userService.getAllMaxWeights(userId);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (MaxWeightNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/maxweight")
    public ResponseEntity<MaxWeightDTO> setMaxWeight(@RequestBody MaxWeightDTO request) {
        try {
            MaxWeightDTO userMaxWeight = userService.setMaxWeight(request.getUserId(), request.getExerciseId(), request.getMaxWeight());
            return new ResponseEntity<>(userMaxWeight, HttpStatus.CREATED);
        } catch (ExerciseNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } catch (MaxWeightAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/maxweight")
    public ResponseEntity<MaxWeightDTO> updateMaxWeights(@RequestBody MaxWeightDTO request) {
        try {
            MaxWeightDTO userMaxWeight = userService.updateMaxWeight(request.getUserId(), request.getExerciseId(), request.getMaxWeight());
            return new ResponseEntity<>(userMaxWeight, HttpStatus.OK);
        } catch (ExerciseNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } catch (MaxWeightNotFoundException e) {
            try {
                MaxWeightDTO userMaxWeight = userService.setMaxWeight(request.getUserId(), request.getExerciseId(), request.getMaxWeight());
                return new ResponseEntity<>(userMaxWeight, HttpStatus.CREATED);
            } catch (UserNotFoundException | ExerciseNotFoundException | MaxWeightAlreadyExistsException e1) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }
    }

    @DeleteMapping("/maxweight/{userId}/{exerciseId}")
    public ResponseEntity<String> deleteMaxWeight(@PathVariable Long userId, @PathVariable Long exerciseId) {
        try {
            userService.deleteMaxWeight(userId, exerciseId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MaxWeightNotFoundException | ExerciseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exercise/{userId}/{muscleGroupId}/{numberOfExercises}")
    public ResponseEntity<List<ExerciseDTO>> getWorkout(@PathVariable Long userId, @PathVariable Long muscleGroupId, @PathVariable int numberOfExercises) {
        try {
            List<ExerciseDTO> exercises = userService.getExercisesFromOneMuscleGroup(muscleGroupId);
            List<ExerciseDTO> selectedExercises = userService.selectRandomExercises(exercises, numberOfExercises);
            for (ExerciseDTO exercise : selectedExercises) {
                if (userService.checkIfMaxWeightExists(userId, exercise.getExerciseId())) {
                    String setsAndReps = userService.selectSetsAndReps(exercise);
                    double percentage = userService.calculatePercentage(setsAndReps);
                    double maxWeight = userService.getMaxWeight(userId, exercise.getExerciseId()).getMaxWeight();
                    double suggestedWeight = maxWeight * percentage;
                    exercise.setSuggestedWeight(String.valueOf(suggestedWeight));
                    exercise.setSetsAndReps(setsAndReps);
                } else if (!userService.checkIfMaxWeightExists(userId, exercise.getExerciseId())) {
                    String setsAndReps = userService.selectSetsAndReps(exercise);
                    exercise.generalSuggestedWeight();
                    exercise.setSetsAndReps(setsAndReps);
                }
            }
            return new ResponseEntity<>(selectedExercises, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (ExerciseNotFoundException | MuscleGroupNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/exercise/{userId}/{numberOfExercises}")
    public ResponseEntity<List<ExerciseDTO>> getRandomWorkout(@PathVariable Long userId, @PathVariable int numberOfExercises) {
        try {
            List<Exercise> allExercises = exerciseRepository.findAll();
            List<ExerciseDTO> allExerciseDTOs = new ArrayList<>();

            for (Exercise exercise : allExercises) {
                ExerciseDTO dto = userService.convertToExerciseDTO(exercise);
                allExerciseDTOs.add(dto);
            }

            if (numberOfExercises > allExerciseDTOs.size()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            List<ExerciseDTO> selectedExercises = userService.selectRandomExercises(allExerciseDTOs, numberOfExercises);
            for (ExerciseDTO exercise : selectedExercises) {
                if (userService.checkIfMaxWeightExists(userId, exercise.getExerciseId())) {
                    String setsAndReps = userService.selectSetsAndReps(exercise);
                    double percentage = userService.calculatePercentage(setsAndReps);
                    double maxWeight = userService.getMaxWeight(userId, exercise.getExerciseId()).getMaxWeight();
                    double suggestedWeight = maxWeight * percentage;
                    double roundedSuggestedWeight = Math.round(suggestedWeight * 10) / 10.0;
                    exercise.setSuggestedWeight(String.valueOf(roundedSuggestedWeight));
                    exercise.setSetsAndReps(setsAndReps);
                } else if (!userService.checkIfMaxWeightExists(userId, exercise.getExerciseId())) {
                    String setsAndReps = userService.selectSetsAndReps(exercise);
                    exercise.generalSuggestedWeight();
                    exercise.setSetsAndReps(setsAndReps);
                }
            }

            return new ResponseEntity<>(selectedExercises, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/musclegroups")
    public ResponseEntity<List<MuscleGroupDTO>> getAllMuscleGroups(){
        try {
            List<MuscleGroupDTO> muscleGroups = userService.getAllMuscleGroups();
            return new ResponseEntity<>(muscleGroups, HttpStatus.OK);
        } catch (MuscleGroupNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
}

