package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.ExerciseDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("User is not authorized to change this password.");
        }
        User user = userRepository.getOne(userId);
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(newPassword);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and be at least 8 characters long");
        }
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public MaxWeightDTO getMaxWeight(Long userId, Long exerciseId) {
        userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        exerciseRepository.findById(exerciseId).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        UserMaxWeight userMaxWeight = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new MaxWeightNotFoundException("Max weight not found"));
        return convertToWeightDTO(userMaxWeight);
    }

    public List<MaxWeightDTO> getAllMaxWeights() {
        List<UserMaxWeight> allUserWeights = userMaxWeightRepository.findAll();
        List<MaxWeightDTO> allUserWeightsDTO = new ArrayList<>();
        for (UserMaxWeight userMaxWeight : allUserWeights) {
            allUserWeightsDTO.add(convertToWeightDTO(userMaxWeight));
        } if (allUserWeightsDTO.isEmpty()) {
            throw new MaxWeightNotFoundException( "No max weights found");
        }
        return allUserWeightsDTO;
    }

    public MaxWeightDTO setMaxWeight(Long userId, Long exerciseId, int maxWeight) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("User is not authorized to modify this max weight");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        Optional<UserMaxWeight> userMaxWeightCheck = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId);
        if (userMaxWeightCheck.isPresent()) {
            throw new MaxWeightAlreadyExistsException("Max weight already exists");
        }
        UserMaxWeight userMaxWeight = new UserMaxWeight();
        userMaxWeight.setUser(userRepository.getOne(userId));
        userMaxWeight.setExercise(exercise);
        userMaxWeight.setMaxWeight(maxWeight);

        UserMaxWeight savedUserMaxWeight = userMaxWeightRepository.save(userMaxWeight);
        return convertToWeightDTO(savedUserMaxWeight);
    }

    public MaxWeightDTO updateMaxWeight(Long userId, Long exerciseId, int maxWeight) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("User is not authorized to modify this max weight");
        }
        exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        UserMaxWeight userMaxWeight = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new MaxWeightNotFoundException("Max weight not found"));
        userMaxWeight.setMaxWeight(maxWeight);
        UserMaxWeight savedUserMaxWeight = userMaxWeightRepository.save(userMaxWeight);
        return convertToWeightDTO(savedUserMaxWeight);
    }

    public MaxWeightDTO convertToWeightDTO(UserMaxWeight userMaxWeight) {
        MaxWeightDTO dto = new MaxWeightDTO();
        dto.setUserId(userMaxWeight.getUser().getUserId());
        dto.setExerciseId(userMaxWeight.getExercise().getExerciseId());
        dto.setMaxWeight(userMaxWeight.getMaxWeight());
        dto.setUsername(userMaxWeight.getUser().getUsername());
        dto.setExerciseName(userMaxWeight.getExercise().getName());
        return dto;
    }

    public List<ExerciseDTO> getExercisesFromOneMuscleGroup(Long muscleGroupId) {
        List<Exercise> exercises = exerciseRepository.findByMuscleGroups_MuscleGroupId(muscleGroupId);
        List<ExerciseDTO> exerciseDTOs = new ArrayList<>();
        for (Exercise exercise : exercises) {
            ExerciseDTO dto = new ExerciseDTO();
            dto.setExerciseId(exercise.getExerciseId());
            dto.setName(exercise.getName());
            exerciseDTOs.add(dto);
        }
        return exerciseDTOs;
    }

    public ExerciseDTO selectRandomExercise(List<ExerciseDTO> exercises) {
        ExerciseDTO selectedExercise = new ExerciseDTO();
        int randomIndex = (int) (Math.random() * exercises.size());
        selectedExercise = exercises.get(randomIndex);
        return selectedExercise;
    }
}
