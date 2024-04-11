package com.fredrikkodar.TrainingPartner.controller;

import com.fredrikkodar.TrainingPartner.dto.ExerciseCreationDTO;
import com.fredrikkodar.TrainingPartner.dto.ExerciseDTO;
import com.fredrikkodar.TrainingPartner.dto.UserDTO;
import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.exceptions.ExerciseAlreadyExistsException;
import com.fredrikkodar.TrainingPartner.exceptions.UnauthorizedException;
import com.fredrikkodar.TrainingPartner.exceptions.UserNotFoundException;
import com.fredrikkodar.TrainingPartner.service.AdminService;
import com.fredrikkodar.TrainingPartner.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = adminService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO>getUserById(@PathVariable Long userId) {
        try {
            UserDTO user = adminService.getUserById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            String message = adminService.deleteUser(userId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/grantadmin/{userId}")
    public ResponseEntity<Void> grantAdminRole(@PathVariable Long userId) {
        try {
            adminService.grantAdminRole(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/exercise")
    public ResponseEntity<ExerciseCreationDTO> createExercise(@RequestBody ExerciseCreationDTO request) {
        logger.info("Received request to create exercise: {}", request);
        try {
            ExerciseCreationDTO exercise = adminService.createExercise(request);
            logger.info("Exercise created successfully: {}", exercise);
            return new ResponseEntity<>(exercise, HttpStatus.CREATED);
        } catch (ExerciseAlreadyExistsException e) {
            logger.error("Exercise already exists: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid arguments: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}


