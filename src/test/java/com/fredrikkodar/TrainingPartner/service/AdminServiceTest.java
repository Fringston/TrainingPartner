package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.ExerciseCreationDTO;
import com.fredrikkodar.TrainingPartner.dto.RoleDTO;
import com.fredrikkodar.TrainingPartner.dto.UserDTO;
import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import com.fredrikkodar.TrainingPartner.entities.Role;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.exceptions.UnauthorizedException;
import com.fredrikkodar.TrainingPartner.repository.ExerciseRepository;
import com.fredrikkodar.TrainingPartner.repository.MuscleGroupRepository;
import com.fredrikkodar.TrainingPartner.repository.RoleRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private MuscleGroupRepository muscleGroupRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private Jwt jwt;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Arrange user1
        user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("user1");

        Role userRole = new Role();
        userRole.setAuthority("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user1.setAuthorities(roles);

        // Arrange user2
        user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("user2");

        userRole.setAuthority("USER");
        Set<Role> roles2 = new HashSet<>();
        roles2.add(userRole);
        user2.setAuthorities(roles2);
    }

    @Test
    void getAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserDTO> users = adminService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    void getUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Act
        UserDTO userDTO = adminService.getUserById(1L);

        // Assert
        assertEquals("user1", userDTO.getUsername());
    }

    @Test
    void deleteUser() {
        //Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Act
        String message = adminService.deleteUser(1L);

        // Assert
        assertEquals("User with id 1 has been deleted", message);
        verify(userRepository, times(1)).delete(user1);
    }

    /*@Test
    void grantAdminRole_Success() {
        // Arrange
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", 1L));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(roleRepository.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role("ADMIN")));

        // Act
        adminService.grantAdminRole(2L);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void grantAdminRole_Unauthorized() {
        // Arrange
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("userId", 2L));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> adminService.grantAdminRole(1L));
    }*/

    @Test
    void createExercise() {
        // Arrange
        ExerciseCreationDTO request = new ExerciseCreationDTO();
        request.setName("Test Exercise");
        Set<Long> muscleGroupIds = new HashSet<>();
        muscleGroupIds.add(1L);
        request.setMuscleGroupIds(muscleGroupIds);

        MuscleGroup muscleGroup = new MuscleGroup();
        muscleGroup.setMuscleGroupId(1L);
        muscleGroup.setName("Test Muscle Group");

        when(exerciseRepository.existsByName(request.getName())).thenReturn(false);
        when(muscleGroupRepository.findById(1L)).thenReturn(Optional.of(muscleGroup));
        when(exerciseRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ExerciseCreationDTO result = adminService.createExercise(request);

        // Assert
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getMuscleGroupIds(), result.getMuscleGroupIds());
    }
}