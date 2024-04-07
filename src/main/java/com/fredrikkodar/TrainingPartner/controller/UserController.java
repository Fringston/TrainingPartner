package com.fredrikkodar.TrainingPartner.controller;

import com.fredrikkodar.TrainingPartner.dto.MaxWeightRequestDTO;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeights;
import com.fredrikkodar.TrainingPartner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String helloUserController() {
        return "User access level";
    }

    @GetMapping("/maxweight/{userId}/{exerciseId}")
    public UserMaxWeights getMaxWeight(@PathVariable Long userId, @PathVariable Long exerciseId) {
        return userService.getMaxWeight(userId, exerciseId);
    }
    @PostMapping("/maxweight")
    public UserMaxWeights setMaxWeight(@RequestBody MaxWeightRequestDTO request) {
        return userService.setMaxWeight(request.getUserId(), request.getExerciseId(), request.getMaxWeight());
    }

}
