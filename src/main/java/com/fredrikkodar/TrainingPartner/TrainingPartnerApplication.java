package com.fredrikkodar.TrainingPartner;

import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import com.fredrikkodar.TrainingPartner.entities.Role;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.MuscleGroupRepository;
import com.fredrikkodar.TrainingPartner.repository.RoleRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class TrainingPartnerApplication {

	public static void main(String[] args) {

		SpringApplication.run(TrainingPartnerApplication.class, args);
	}

	@Bean
	CommandLineRunner initUserRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			User admin = new User(1L, "admin", passwordEncoder.encode("password"), roles);

			userRepository.save(admin);
		};
	}

	@Bean
	CommandLineRunner initMuscleGroups(MuscleGroupRepository muscleGroupRepository) {
		return args -> {
			List<MuscleGroup> muscleGroups = new ArrayList<>();
			if (!muscleGroupRepository.existsByName("Arms")) muscleGroups.add(new MuscleGroup("Arms"));
			if (!muscleGroupRepository.existsByName("Chest")) muscleGroups.add(new MuscleGroup("Chest"));
			if (!muscleGroupRepository.existsByName("Shoulders")) muscleGroups.add(new MuscleGroup("Shoulders"));
			if (!muscleGroupRepository.existsByName("Upper back")) muscleGroups.add(new MuscleGroup("Upper back"));
			if (!muscleGroupRepository.existsByName("Lower back")) muscleGroups.add(new MuscleGroup("Lower back"));
			if (!muscleGroupRepository.existsByName("Glutes")) muscleGroups.add(new MuscleGroup("Glutes"));
			if (!muscleGroupRepository.existsByName("Legs")) muscleGroups.add(new MuscleGroup("Legs"));
			if (!muscleGroupRepository.existsByName("Abs")) muscleGroups.add(new MuscleGroup("Abs"));

			muscleGroupRepository.saveAll(muscleGroups);
		};
	}

	@Bean
	CommandLineRunner initExercises(MuscleGroupRepository muscleGroupRepository, ExerciseRepository exerciseRepository) {

		return args -> {
			if (!exerciseRepository.existsByName("Bicep curls")) {
				Exercise exercise = new Exercise("Bicep curls");
				muscleGroupRepository.findByName("Arms")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Arms")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Triceps extensions")) {
				Exercise exercise = new Exercise("Triceps extensions");
				muscleGroupRepository.findByName("Arms")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Arms")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Bench press")) {
				Exercise exercise = new Exercise("Bench press");
				muscleGroupRepository.findByName("Chest")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Chest")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Shoulder press")) {
				Exercise exercise = new Exercise("Shoulder press");
				muscleGroupRepository.findByName("Shoulders")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Shoulders")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Pull-ups")) {
				Exercise exercise = new Exercise("Pull-ups");
				muscleGroupRepository.findByName("Upper back")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Upper back")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Deadlift")) {
				Exercise exercise = new Exercise("Deadlift");
				muscleGroupRepository.findByName("Lower back")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Lower back")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Hip thrusts")) {
				Exercise exercise = new Exercise("Hip thrusts");
				muscleGroupRepository.findByName("Glutes")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Glutes")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Back squats")) {
				Exercise exercise = new Exercise("Back squats");
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Front squats")) {
				Exercise exercise = new Exercise("Front squats");
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Lunges")) {
				Exercise exercise = new Exercise("Lunges");
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Sit ups")) {
				Exercise exercise = new Exercise("Sit ups");
				muscleGroupRepository.findByName("Abs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Abs")
						);
				exerciseRepository.save(exercise);
			}
		};
	}

	private void handleMissingMuscleGroup(String muscleGroupName) {
		throw new RuntimeException("Muscle group '" + muscleGroupName + "' not found");
	}

}
