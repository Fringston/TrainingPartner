package com.fredrikkodar.TrainingPartner.controller;

import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import com.fredrikkodar.TrainingPartner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /*@GetMapping("/maxweight/{userId}/{exerciseId}")
    public UserMaxWeight getMaxWeight(@PathVariable Long userId, @PathVariable Long exerciseId) {
        return userService.getMaxWeight(userId, exerciseId);
    }
    @GetMapping("/maxweights")
    public List<UserMaxWeight> getAllMaxWeights() {
        return userService.getAllMaxWeights();
    }*/

    @GetMapping("/maxweight/{userId}/{exerciseId}")
    public MaxWeightDTO getMaxWeight(@PathVariable Long userId, @PathVariable Long exerciseId) {
        return userService.getMaxWeight(userId, exerciseId);
    }
    @GetMapping("/maxweights")
    public List<MaxWeightDTO> getAllMaxWeights() {
        return userService.getAllMaxWeights();
    }
    @PostMapping("/maxweight")
    public UserMaxWeight setMaxWeight(@RequestBody MaxWeightDTO request) {
        return userService.setMaxWeight(request.getUserId(), request.getExerciseId(), request.getMaxWeight());
    }

}
