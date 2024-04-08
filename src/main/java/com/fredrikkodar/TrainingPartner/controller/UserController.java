package com.fredrikkodar.TrainingPartner.controller;

import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.dto.PasswordChangeDTO;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import com.fredrikkodar.TrainingPartner.exceptions.*;
import com.fredrikkodar.TrainingPartner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

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
        } catch (UserNotFoundException | ExerciseNotFoundException | MaxWeightNotFoundException e) {
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

}
