package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.ExerciseDTO;
import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.dto.UserDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public UserDTO getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToUserDTO(user);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("User not authorized");
        }
        userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        exerciseRepository.findById(exerciseId).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        UserMaxWeight userMaxWeight = userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new MaxWeightNotFoundException("Max weight not found"));
        return convertToWeightDTO(userMaxWeight);
    }

    public List<MaxWeightDTO> getAllMaxWeights(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("User is not authorized to modify this max weight");
        }
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

    @Transactional
    public void deleteMaxWeight(Long userId, Long exerciseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("User is not authorized to delete this max weight");
        }
        exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
                .orElseThrow(() -> new MaxWeightNotFoundException("Max weight not found"));
        userMaxWeightRepository.deleteByUser_UserIdAndExercise_ExerciseId(userId, exerciseId);
    }

    public List<ExerciseDTO> getExercisesFromOneMuscleGroup(Long muscleGroupId) {
        List<Exercise> exercises = exerciseRepository.findByMuscleGroups_MuscleGroupId(muscleGroupId);
        List<ExerciseDTO> exerciseDTOs = new ArrayList<>();
        for (Exercise exercise : exercises) {
            ExerciseDTO dto = new ExerciseDTO();
            dto.setExerciseId(exercise.getExerciseId());
            dto.setName(exercise.getName());
            Set<Long> muscleGroupIds = new HashSet<>();
            muscleGroupIds.add(muscleGroupId);
            dto.setMuscleGroupId(muscleGroupIds);
            exerciseDTOs.add(dto);
        }
        return exerciseDTOs;
    }

    public List<ExerciseDTO> selectRandomExercises(List<ExerciseDTO> exercises, int numberOfExercises) {
        if (numberOfExercises > exercises.size()) {
            throw new IllegalArgumentException("Number of exercises to select cannot be greater than the total number of exercises");
        }

        List<ExerciseDTO> selectedExercises = new ArrayList<>();
        for (int i = 0; i < numberOfExercises; i++) {
            int randomIndex = (int) (Math.random() * exercises.size());
            selectedExercises.add(exercises.get(randomIndex));
            exercises.remove(randomIndex);
        }
        return selectedExercises;
    }

    public boolean checkIfMaxWeightExists(Long userId, Long exerciseId) {
        return userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId).isPresent();
    }

    public String selectSetsAndReps(ExerciseDTO exerciseDTO) {
        ExerciseDTO exercise = new ExerciseDTO();
        int reps;
        int sets;
        String[] possibleRepsAndSets = {"3x12", "4x10", "5x8", "6x6", "5x5", "6x4", "4x2"};
        String setsAndReps = possibleRepsAndSets[(int) (Math.random() * possibleRepsAndSets.length)];
        switch(setsAndReps) {
            case "3x12":
                sets = 3;
                reps = 12;
                break;
            case "4x10":
                sets = 4;
                reps = 10;
                break;
            case "5x8":
                sets = 5;
                reps = 8;
                break;
            case "6x6":
                sets = 6;
                reps = 6;
                break;
            case "5x5":
                sets = 5;
                reps = 5;
                break;
            case "6x4":
                sets = 6;
                reps = 4;
                break;
            case "4x2":
                sets = 4;
                reps = 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid number of reps and sets");

        }
        return sets + "x" + reps;
    }

    public double calculatePercentage(String repsAndSets) {
        switch(repsAndSets) {
            case "3x12":
                return 0.6;
            case "4x10":
                return 0.7;
            case "5x8":
                return 0.75;
            case "6x6":
                return 0.8;
            case "5x5":
                return 0.85;
            case "6x4":
                return 0.9;
            case "4x2":
                return 0.95;
            default:
                throw new IllegalArgumentException("Invalid number of reps and sets");
        }
    }

    private MaxWeightDTO convertToWeightDTO(UserMaxWeight userMaxWeight) {
        MaxWeightDTO dto = new MaxWeightDTO();
        dto.setUserId(userMaxWeight.getUser().getUserId());
        dto.setExerciseId(userMaxWeight.getExercise().getExerciseId());
        dto.setMaxWeight(userMaxWeight.getMaxWeight());
        dto.setUsername(userMaxWeight.getUser().getUsername());
        dto.setExerciseName(userMaxWeight.getExercise().getName());
        return dto;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

}
