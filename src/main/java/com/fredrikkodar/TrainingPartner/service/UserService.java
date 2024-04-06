package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.entities.UserMaxWeights;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.UserMaxWeightsRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserMaxWeightsRepository userMaxWeightsRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("In the User Detail Service");

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public UserMaxWeights setMaxWeight(Long userId, Long exerciseId, int maxWeight) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        UserMaxWeights userMaxWeights = new UserMaxWeights();
        userMaxWeights.setUser(user);
        userMaxWeights.setExercise(exercise);
        userMaxWeights.setMaxWeight(maxWeight);

        return userMaxWeightsRepository.save(userMaxWeights);
    }
}
