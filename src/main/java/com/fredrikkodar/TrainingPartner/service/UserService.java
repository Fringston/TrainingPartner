package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.UserMaxWeightRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserMaxWeightRepository userMaxWeightRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("In the User Detail Service");

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    /*public UserMaxWeight getMaxWeight(Long userId, Long exerciseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new RuntimeException("Max weight not found"));
    }

    public List<UserMaxWeight> getAllMaxWeights() {
        List<UserMaxWeight> allUserWeights = userMaxWeightRepository.findAll();
        for (UserMaxWeight userMaxWeight : allUserWeights) {
            if (userMaxWeight == null) {
                System.out.println("No max weights found");
            }
            System.out.println(userMaxWeight);
        }
        return userMaxWeightRepository.findAll();
    }*/

    public MaxWeightDTO getMaxWeight(Long userId, Long exerciseId) {
        UserMaxWeight userMaxWeight = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new RuntimeException("Max weight not found"));
        return convertToDTO(userMaxWeight);
    }

    public List<MaxWeightDTO> getAllMaxWeights() {
        List<UserMaxWeight> allUserWeights = userMaxWeightRepository.findAll();
        List<MaxWeightDTO> allUserWeightsDTO = new ArrayList<>();
        for (UserMaxWeight userMaxWeight : allUserWeights) {
            allUserWeightsDTO.add(convertToDTO(userMaxWeight));
        }
        return allUserWeightsDTO;
    }

    public UserMaxWeight setMaxWeight(Long userId, Long exerciseId, int maxWeight) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        UserMaxWeight userMaxWeight = new UserMaxWeight();
        userMaxWeight.setUser(user);
        userMaxWeight.setExercise(exercise);
        userMaxWeight.setMaxWeight(maxWeight);

        return userMaxWeightRepository.save(userMaxWeight);
    }

    public MaxWeightDTO convertToDTO(UserMaxWeight userMaxWeight) {
        MaxWeightDTO dto = new MaxWeightDTO();
        dto.setUserId(userMaxWeight.getUser().getUserId());
        dto.setExerciseId(userMaxWeight.getExercise().getExerciseId());
        dto.setMaxWeight(userMaxWeight.getMaxWeight());
        dto.setUsername(userMaxWeight.getUser().getUsername());
        dto.setExerciseName(userMaxWeight.getExercise().getName());
        return dto;
    }
}
