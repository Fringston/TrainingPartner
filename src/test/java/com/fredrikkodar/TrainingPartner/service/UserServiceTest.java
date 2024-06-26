package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.ExerciseDTO;
import com.fredrikkodar.TrainingPartner.dto.MaxWeightDTO;
import com.fredrikkodar.TrainingPartner.dto.MuscleGroupDTO;
import com.fredrikkodar.TrainingPartner.dto.UserDTO;
import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeight;
import com.fredrikkodar.TrainingPartner.exceptions.MaxWeightAlreadyExistsException;
import com.fredrikkodar.TrainingPartner.exceptions.MaxWeightNotFoundException;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.MuscleGroupRepository;
import com.fredrikkodar.TrainingPartner.repository.UserMaxWeightRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import static org.mockito.ArgumentMatchers.any;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserMaxWeightRepository userMaxWeightRepository;
    @Mock
    private MuscleGroupRepository muscleGroupRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private Long userId;
    private User user;
    private Long exerciseId;
    private Exercise exercise;
    private UserMaxWeight userMaxWeight;
    private MuscleGroup muscleGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        userId = 1L;
        user = new User();
        user.setUserId(userId);
        user.setUsername("testUser");

        exerciseId = 1L;
        exercise = new Exercise();
        exercise.setExerciseId(exerciseId);
        exercise.setName("Test Exercise");
        exercise.setPossibleMaxWeight(true);

        muscleGroup = new MuscleGroup();
        muscleGroup.setMuscleGroupId(1L);
        muscleGroup.setName("Test Muscle Group");
        exercise.setMuscleGroups(Collections.singleton(muscleGroup));
        muscleGroup.setExercises(Collections.singleton(exercise));

        userMaxWeight = new UserMaxWeight();
        userMaxWeight.setUser(user);
        userMaxWeight.setExercise(exercise);
        userMaxWeight.setMaxWeight(100);

        // Stub the findByUser_UserId method
        when(userMaxWeightRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userMaxWeight));

        // Stub the findAll method of exerciseRepository
        when(exerciseRepository.findAll()).thenReturn(Collections.singletonList(exercise));
    }

    @Test
    void loadUserByUsername() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userService.loadUserByUsername(username);

        // Assert
        assertEquals(username, result.getUsername());
    }

    @Test
    void loadUserByUsername_NotFound() {
        // Arrange
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void getUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUser(userId);

        // Assert
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void getUser_NotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void changePassword() {
        // Arrange
        String oldPassword = "oldPassword";
        String newPassword = "NewPassword@1234";
        when(userRepository.getOne(userId)).thenReturn(user);
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act and Assert
        assertDoesNotThrow(() -> userService.changePassword(userId, oldPassword, newPassword));
        }

    @Test
    void changePassword_WrongOldPassword() {
        // Arrange
            String oldPassword = "wrongOldPassword";
            String newPassword = "NewPassword@1234";
            when(userRepository.getOne(userId)).thenReturn(user);
            when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

            // Mocking the authentication
            Authentication auth = mock(Authentication.class);
            Jwt jwt = mock(Jwt.class);
            when(auth.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Act and Assert
            assertThrows(IllegalArgumentException.class, () -> userService.changePassword(userId, oldPassword, newPassword));
    }

    @Test
    void getMaxWeight() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.of(userMaxWeight));

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        MaxWeightDTO result = userService.getMaxWeight(userId, exerciseId);

        // Assert
        assertEquals(userMaxWeight.getMaxWeight(), result.getMaxWeight());
    }

    @Test
    void getMaxWeight_NotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getMaxWeight(userId, exerciseId));
    }

    @Test
    void getAllMaxWeights() {
        // Arrange
        List<UserMaxWeight> allUserWeights = new ArrayList<>();
        allUserWeights.add(userMaxWeight);
        when(userMaxWeightRepository.findByUser_UserId(userId)).thenReturn(allUserWeights);

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        List<MaxWeightDTO> result = userService.getAllMaxWeights(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(userMaxWeight.getMaxWeight(), result.get(0).getMaxWeight());
    }

    @Test
    void getAllMaxWeights_Empty() {
        // Arrange
        when(userMaxWeightRepository.findByUser_UserId(userId)).thenReturn(new ArrayList<>());

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act and Assert
        assertThrows(MaxWeightNotFoundException.class, () -> userService.getAllMaxWeights(userId));
    }

    @Test
    void setMaxWeight() {
        // Arrange
        int maxWeight = 200;
        userMaxWeight.setMaxWeight(maxWeight); // Set maxWeight of userMaxWeight to 200
        when(userRepository.getOne(userId)).thenReturn(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.empty());
        when(userMaxWeightRepository.save(any(UserMaxWeight.class))).thenReturn(userMaxWeight);

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        MaxWeightDTO result = userService.setMaxWeight(userId, exerciseId, maxWeight);

        // Assert
        assertEquals(userId, result.getUserId());
        assertEquals(exerciseId, result.getExerciseId());
        assertEquals(maxWeight, result.getMaxWeight());
    }

    @Test
    void setMaxWeight_MaxWeightAlreadyExists() {
        // Arrange
        int maxWeight = 200;
        when(userRepository.getOne(userId)).thenReturn(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.of(userMaxWeight));

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act and Assert
        assertThrows(MaxWeightAlreadyExistsException.class, () -> userService.setMaxWeight(userId, exerciseId, maxWeight));
    }

    @Test
    void updateMaxWeight() {
        // Arrange
        int newMaxWeight = 200;
        when(userRepository.getOne(userId)).thenReturn(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.of(userMaxWeight));
        when(userMaxWeightRepository.save(any(UserMaxWeight.class))).thenReturn(userMaxWeight);

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        MaxWeightDTO result = userService.updateMaxWeight(userId, exerciseId, newMaxWeight);

        // Assert
        assertEquals(userId, result.getUserId());
        assertEquals(exerciseId, result.getExerciseId());
        assertEquals(newMaxWeight, result.getMaxWeight());
    }

    @Test
    void deleteMaxWeight() {
        // Arrange
        when(userRepository.getOne(userId)).thenReturn(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.of(userMaxWeight));

        // Mocking the authentication
        Authentication auth = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", userId));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        userService.deleteMaxWeight(userId, exerciseId);

        // Assert
        verify(userMaxWeightRepository, times(1)).deleteByUser_UserIdAndExercise_ExerciseId(userId, exerciseId);
    }

    @Test
    void getExercisesFromOneMuscleGroup() {
        // Arrange
        Long muscleGroupId = 1L;
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        when(exerciseRepository.findByMuscleGroups_MuscleGroupId(muscleGroupId)).thenReturn(exercises);

        // Act
        List<ExerciseDTO> result = userService.getExercisesFromOneMuscleGroup(muscleGroupId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(exerciseId, result.get(0).getExerciseId());
        assertTrue(result.get(0).getMuscleGroupId().contains(muscleGroupId));
    }

    @Test
    void selectRandomExercises() {
        // Arrange
        int numberOfExercises = 2;
        List<ExerciseDTO> exercises = new ArrayList<>();
        ExerciseDTO exercise1 = new ExerciseDTO();
        exercise1.setExerciseId(1L);
        exercises.add(exercise1);
        ExerciseDTO exercise2 = new ExerciseDTO();
        exercise2.setExerciseId(2L);
        exercises.add(exercise2);
        ExerciseDTO exercise3 = new ExerciseDTO();
        exercise3.setExerciseId(3L);
        exercises.add(exercise3);

        // Act
        List<ExerciseDTO> result = userService.selectRandomExercises(exercises, numberOfExercises);

        // Assert
        assertEquals(numberOfExercises, result.size());
    }

    @Test
    void selectRandomExercises_ThrowsException() {
        // Arrange
        int numberOfExercises = 4;
        List<ExerciseDTO> exercises = new ArrayList<>();
        ExerciseDTO exercise1 = new ExerciseDTO();
        exercise1.setExerciseId(1L);
        exercises.add(exercise1);
        ExerciseDTO exercise2 = new ExerciseDTO();
        exercise2.setExerciseId(2L);
        exercises.add(exercise2);
        ExerciseDTO exercise3 = new ExerciseDTO();
        exercise3.setExerciseId(3L);
        exercises.add(exercise3);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.selectRandomExercises(exercises, numberOfExercises));
    }

    @Test
    void checkIfMaxWeightExists() {
        // Arrange
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.of(userMaxWeight));

        // Act
        boolean result = userService.checkIfMaxWeightExists(userId, exerciseId);

        // Assert
        assertTrue(result);
    }

    @Test
    void checkIfMaxWeightExists_NotFound() {
        // Arrange
        when(userMaxWeightRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.checkIfMaxWeightExists(userId, exerciseId);

        // Assert
        assertFalse(result);
    }

    @Test
    void selectSetsAndReps() {
        // Arrange
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseId(exerciseId);

        // Act
        String result = userService.selectSetsAndReps(exerciseDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result.matches("\\d+x\\d+"));
    }

    @Test
    void calculatePercentage() {
        // Arrange
        String repsAndSets1 = "3x12";
        String repsAndSets2 = "4x10";
        String repsAndSets3 = "5x8";
        String repsAndSets4 = "6x6";
        String repsAndSets5 = "5x5";
        String repsAndSets6 = "6x4";
        String repsAndSets7 = "4x2";
        String repsAndSetsInvalid = "invalid";

        // Act and Assert
        assertEquals(0.6, userService.calculatePercentage(repsAndSets1));
        assertEquals(0.7, userService.calculatePercentage(repsAndSets2));
        assertEquals(0.75, userService.calculatePercentage(repsAndSets3));
        assertEquals(0.8, userService.calculatePercentage(repsAndSets4));
        assertEquals(0.85, userService.calculatePercentage(repsAndSets5));
        assertEquals(0.9, userService.calculatePercentage(repsAndSets6));
        assertEquals(0.95, userService.calculatePercentage(repsAndSets7));
        assertThrows(IllegalArgumentException.class, () -> userService.calculatePercentage(repsAndSetsInvalid));
    }
    @Test
    void getAllMuscleGroups() {
        // Arrange
        MuscleGroup muscleGroup1 = new MuscleGroup();
        muscleGroup1.setMuscleGroupId(1L);
        muscleGroup1.setName("Muscle Group 1");

        MuscleGroup muscleGroup2 = new MuscleGroup();
        muscleGroup2.setMuscleGroupId(2L);
        muscleGroup2.setName("Muscle Group 2");

        List<MuscleGroup> muscleGroups = Arrays.asList(muscleGroup1, muscleGroup2);
        when(muscleGroupRepository.findAll()).thenReturn(muscleGroups);

        // Act
        List<MuscleGroupDTO> result = userService.getAllMuscleGroups();

        // Assert
        assertEquals(2, result.size());
        assertEquals(muscleGroup1.getMuscleGroupId(), result.get(0).getMuscleGroupId());
        assertEquals(muscleGroup1.getName(), result.get(0).getName());
        assertEquals(muscleGroup2.getMuscleGroupId(), result.get(1).getMuscleGroupId());
        assertEquals(muscleGroup2.getName(), result.get(1).getName());
    }

    @Test
    void getExercisesWithPossibleMaxWeight() {
        // Act
        List<ExerciseDTO> result = userService.getExercisesWithPossibleMaxWeight();

        // Assert
        assertEquals(1, result.size());
        ExerciseDTO resultExercise = result.get(0);
        assertEquals(exercise.getExerciseId(), resultExercise.getExerciseId());
        assertEquals(exercise.getName(), resultExercise.getName());
        assertTrue(resultExercise.isPossibleMaxWeight());
        assertTrue(resultExercise.getMuscleGroupId().contains(muscleGroup.getMuscleGroupId()));
    }
    @Test
    void convertToExerciseDTO() {
        // Act
        ExerciseDTO result = userService.convertToExerciseDTO(exercise);

        // Assert
        assertEquals(exercise.getExerciseId(), result.getExerciseId());
        assertEquals(exercise.getName(), result.getName());
        assertEquals(exercise.isPossibleMaxWeight(), result.isPossibleMaxWeight());
        assertTrue(result.getMuscleGroupId().contains(muscleGroup.getMuscleGroupId()));
    }
    @Test
    void convertToMuscleGroupDTO() {
        // Act
        MuscleGroupDTO result = userService.convertToMuscleGroupDTO(muscleGroup);

        // Assert
        assertEquals(muscleGroup.getMuscleGroupId(), result.getMuscleGroupId());
        assertEquals(muscleGroup.getName(), result.getName());
        assertEquals(1, result.getExercises().size());

        ExerciseDTO resultExercise = result.getExercises().get(0);
        assertEquals(exercise.getExerciseId(), resultExercise.getExerciseId());
        assertEquals(exercise.getName(), resultExercise.getName());
        assertEquals(exercise.isPossibleMaxWeight(), resultExercise.isPossibleMaxWeight());
    }
}