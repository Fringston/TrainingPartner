package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import com.fredrikkodar.TrainingPartner.exceptions.ExerciseNotFoundException;
import com.fredrikkodar.TrainingPartner.exceptions.MaxWeightAlreadyExistsException;
import com.fredrikkodar.TrainingPartner.exceptions.MaxWeightNotFoundException;
import com.fredrikkodar.TrainingPartner.exceptions.UnauthorizedException;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.UserMaxWeightRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public MaxWeightDTO getMaxWeight(Long userId, Long exerciseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        UserMaxWeight userMaxWeight = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new MaxWeightNotFoundException("Max weight not found"));
        return convertToDTO(userMaxWeight);
    }

    public List<MaxWeightDTO> getAllMaxWeights() {
        List<UserMaxWeight> allUserWeights = userMaxWeightRepository.findAll();
        List<MaxWeightDTO> allUserWeightsDTO = new ArrayList<>();
        for (UserMaxWeight userMaxWeight : allUserWeights) {
            allUserWeightsDTO.add(convertToDTO(userMaxWeight));
        } if (allUserWeightsDTO.isEmpty()) {
            throw new MaxWeightNotFoundException( "No max weights found");
        }
        return allUserWeightsDTO;
    }

    public MaxWeightDTO setMaxWeight(Long userId, Long exerciseId, int maxWeight) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!currentUser.getUserId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to modify this max weight");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        Optional<UserMaxWeight> userMaxWeightCheck = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId);
        if (userMaxWeightCheck.isPresent()) {
            throw new MaxWeightAlreadyExistsException("Max weight already exists");
        }
        UserMaxWeight userMaxWeight = new UserMaxWeight();
        userMaxWeight.setUser(currentUser);
        userMaxWeight.setExercise(exercise);
        userMaxWeight.setMaxWeight(maxWeight);

        UserMaxWeight savedUserMaxWeight = userMaxWeightRepository.save(userMaxWeight);
        return convertToDTO(savedUserMaxWeight);
    }

    public MaxWeightDTO updateMaxWeight(Long userId, Long exerciseId, int maxWeight) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!currentUser.getUserId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to modify this max weight");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        UserMaxWeight userMaxWeight = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new MaxWeightNotFoundException("Max weight not found"));
        userMaxWeight.setMaxWeight(maxWeight);
        UserMaxWeight savedUserMaxWeight = userMaxWeightRepository.save(userMaxWeight);
        return convertToDTO(savedUserMaxWeight);
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
