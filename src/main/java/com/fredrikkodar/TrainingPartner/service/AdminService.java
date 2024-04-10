package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.ExerciseDTO;
import com.fredrikkodar.TrainingPartner.dto.RoleDTO;
import com.fredrikkodar.TrainingPartner.dto.UserDTO;
import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import com.fredrikkodar.TrainingPartner.entities.Role;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.exceptions.ExerciseAlreadyExistsException;
import com.fredrikkodar.TrainingPartner.exceptions.UnauthorizedException;
import com.fredrikkodar.TrainingPartner.exceptions.UserNotFoundException;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.RoleRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;

    public List<UserDTO> getAllUsers() {
    List<User> allUsers = userRepository.findAll();
    List<UserDTO> allUsersDTO = new ArrayList<>();
    for (User user : allUsers) {
        allUsersDTO.add(convertToUserDTO(user));
    } if (allUsersDTO.isEmpty()) {
        throw new UserNotFoundException("No users found");
        }
        return allUsersDTO;
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return convertToUserDTO(user);
    }

    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        userRepository.delete(user);
        return "User with id " + userId + " has been deleted";
    }

    public void grantAdminRole(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) ((Jwt) auth.getPrincipal()).getClaims().get("userId");

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!currentUser.getAuthorities().contains(new Role("ADMIN"))) {
            throw new UnauthorizedException("User is not authorized to grant admin role");
        }
        User userToGrant = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Directly get the admin role from the database
        Role adminAuthority = roleRepository.findByAuthority("ADMIN").get();

        // Clear the existing roles
        userToGrant.getAuthorities().clear();

        Set<Role> authorities = new HashSet<>();
        for (GrantedAuthority authority : userToGrant.getAuthorities()) {
            if (authority instanceof Role) {
                authorities.add((Role) authority);
            }
        }
        authorities.add(adminAuthority);
        userToGrant.setAuthorities(authorities);

        userRepository.save(userToGrant);
    }

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public ExerciseDTO createExercise(String name, Set<MuscleGroup> muscleGroups) {
        logger.info("Creating exercise with name: {} and muscle groups: {}", name, muscleGroups);
        Exercise exercise = new Exercise();
        if (name == null || name.isEmpty()) {
            logger.error("Invalid argument: Name cannot be null or empty");
            throw new IllegalArgumentException("Name cannot be null or empty");
        } else if (exerciseRepository.existsByName(name)) {
            logger.error("Exercise already exists with name: {}", name);
            throw new ExerciseAlreadyExistsException("Exercise with name " + name + " already exists");
        } else {
            exercise.setName(name);
        }
        if (muscleGroups == null || muscleGroups.isEmpty()) {
            logger.error("Invalid argument: Muscle groups cannot be null or empty");
            throw new IllegalArgumentException("Muscle groups cannot be null or empty");
        } else {
            exercise.setMuscleGroups(muscleGroups);
        }
        ExerciseDTO createdExercise = convertToExerciseDTO(exerciseRepository.save(exercise));
        logger.info("Exercise created successfully: {}", createdExercise);
        return createdExercise;
    }



    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());

        Set<RoleDTO> roleDTOs = user.getAuthorities().stream()
                .map(role -> {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setAuthority(role.getAuthority());
                    return roleDTO;
                })
                .collect(Collectors.toSet());

        userDTO.setRoles(roleDTOs);
        return userDTO;
    }

    private ExerciseDTO convertToExerciseDTO(Exercise exercise) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseId(exercise.getExerciseId());
        exerciseDTO.setName(exercise.getName());
        exerciseDTO.setMuscleGroups(exercise.getMuscleGroups());
        return exerciseDTO;
    }

}
