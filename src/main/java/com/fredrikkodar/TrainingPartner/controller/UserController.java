package com.fredrikkodar.TrainingPartner.controller;

import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import com.fredrikkodar.TrainingPartner.exceptions.ExerciseNotFoundException;
import com.fredrikkodar.TrainingPartner.exceptions.MaxWeightAlreadyExistsException;
import com.fredrikkodar.TrainingPartner.exceptions.MaxWeightNotFoundException;
import com.fredrikkodar.TrainingPartner.exceptions.UserNotFoundException;
import com.fredrikkodar.TrainingPartner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/maxweight/{userId}/{exerciseId}")
    public ResponseEntity<MaxWeightDTO> getMaxWeight(@PathVariable Long userId, @PathVariable Long exerciseId) {
        try {
            MaxWeightDTO dto = userService.getMaxWeight(userId, exerciseId);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (UserNotFoundException | ExerciseNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/maxweights")
    public ResponseEntity<List<MaxWeightDTO>> getAllMaxWeights() {
        try {
            List<MaxWeightDTO> dtos = userService.getAllMaxWeights();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (MaxWeightNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/maxweight")
    public ResponseEntity<UserMaxWeight> setMaxWeight(@RequestBody MaxWeightDTO request) {
        try {
            UserMaxWeight userMaxWeight = userService.setMaxWeight(request.getUserId(), request.getExerciseId(), request.getMaxWeight());
            return new ResponseEntity<>(userMaxWeight, HttpStatus.CREATED);
        } catch (UserNotFoundException | ExerciseNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (MaxWeightAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

}
