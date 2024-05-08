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
			if (!muscleGroupRepository.existsByName("Biceps")) muscleGroups.add(new MuscleGroup("Biceps"));
			if (!muscleGroupRepository.existsByName("Triceps")) muscleGroups.add(new MuscleGroup("Triceps"));
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
			if (!exerciseRepository.existsByName("Barbell bicep curl")) {
				Exercise exercise = new Exercise("Barbell Bicep curl");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Biceps")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Biceps")
						);
				exerciseRepository.save(exercise);
			}
				if (!exerciseRepository.existsByName("Dumbbell bicep curl")) {
					Exercise exercise = new Exercise("Dumbbell bicep curl");
					exercise.setPossibleMaxWeight(false);
					muscleGroupRepository.findByName("Biceps")
							.ifPresentOrElse(
									muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
									() -> handleMissingMuscleGroup("Biceps")
							);
					exerciseRepository.save(exercise);
				}
			if (!exerciseRepository.existsByName("Skull crusher")) {
				Exercise exercise = new Exercise("Skull crusher");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Triceps")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Triceps")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Dips")) {
				Exercise exercise = new Exercise("Dips");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Triceps")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Triceps")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Barbell bench press")) {
				Exercise exercise = new Exercise("Barbell bench press");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Chest")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Chest")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Dumbbell bench press")) {
				Exercise exercise = new Exercise("Dumbbell bench press");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Chest")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Chest")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Barbell shoulder press")) {
				Exercise exercise = new Exercise("Barbell shoulder press");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Shoulders")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Shoulders")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Dumbbell shoulder press")) {
				Exercise exercise = new Exercise("Dumbbell shoulder press");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Shoulders")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Shoulders")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Dumbbell lateral raise")) {
				Exercise exercise = new Exercise("Dumbbell lateral raise");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Shoulders")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Shoulders")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Barbell push press")) {
				Exercise exercise = new Exercise("Barbell push press");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Shoulders")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Shoulders")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Handstand push up")) {
				Exercise exercise = new Exercise("Handstand push up");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Shoulders")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Shoulders")
						);
				exerciseRepository.save(exercise);
			}

			if (!exerciseRepository.existsByName("Pull-up")) {
				Exercise exercise = new Exercise("Pull-up");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Upper back")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Upper back")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Deadlift")) {
				Exercise exercise = new Exercise("Deadlift");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Lower back")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Lower back")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Romanian deadlift")) {
				Exercise exercise = new Exercise("Romanian deadlift");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Lower back")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Lower back")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Bent over row")) {
				Exercise exercise = new Exercise("Bent over row");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Lower back")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Lower back")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Hip thrust")) {
				Exercise exercise = new Exercise("Hip thrust");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Glutes")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Glutes")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Back squat")) {
				Exercise exercise = new Exercise("Back squat");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Front squat")) {
				Exercise exercise = new Exercise("Front squat");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Overhead squat")) {
				Exercise exercise = new Exercise("Overhead squat");
				exercise.setPossibleMaxWeight(true);
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Dumbbell Lunges")) {
				Exercise exercise = new Exercise("Dumbell Lunges");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Dumbbell Bulgarian split squat")) {
				Exercise exercise = new Exercise("Dumbbell Bulgarian split squat");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Legs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Legs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Sit up")) {
				Exercise exercise = new Exercise("Sit up");
				exercise.setPossibleMaxWeight(false);
				muscleGroupRepository.findByName("Abs")
						.ifPresentOrElse(
								muscleGroup -> exercise.getMuscleGroups().add(muscleGroup),
								() -> handleMissingMuscleGroup("Abs")
						);
				exerciseRepository.save(exercise);
			}
			if (!exerciseRepository.existsByName("Strict toes to bar")) {
				Exercise exercise = new Exercise("Strict toes to bar");
				exercise.setPossibleMaxWeight(false);
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
