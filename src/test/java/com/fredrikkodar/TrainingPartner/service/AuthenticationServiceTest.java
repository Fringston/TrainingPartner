package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.repository.RoleRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import com.fredrikkodar.TrainingPartner.dto.LoginResponseDTO;
import com.fredrikkodar.TrainingPartner.entities.Role;
import com.fredrikkodar.TrainingPartner.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

    public class AuthenticationServiceTest {

        @InjectMocks
        private AuthenticationService authenticationService;
        @Mock
        private UserRepository userRepository;
        @Mock
        private RoleRepository roleRepository;
        @Mock
        private PasswordEncoder passwordEncoder;
        @Mock
        private AuthenticationManager authenticationManager;
        @Mock
        private TokenService tokenService;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        /*@Test
        public void testRegisterUser() {
            // Arrange
            String username = "testUser";
            String password = "Test@1234";
            String encodedPassword = "encodedPassword";
            User user = new User(0L, username, encodedPassword, new HashSet<>());
            when(passwordEncoder.encode(any(String.class))).thenReturn(encodedPassword);
            when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(new Role("USER")));
            when(userRepository.save(any(User.class))).thenReturn(user);

            // Act
            User result = authenticationService.registerUser(username, password);

            // Assert
            assertEquals(username, result.getUsername());
            assertEquals(encodedPassword, result.getPassword());
        }

        @Test
        public void testLoginUser() {
            // Arrange
            String username = "testUser";
            String password = "Test@1234";
            String token = "token";
            User user = new User(0L, username, password, new HashSet<>());
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
            when(tokenService.generateJwt(auth)).thenReturn(token);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            // Act
            LoginResponseDTO result = authenticationService.loginUser(username, password);

            // Assert
            assertEquals(username, result.getUser().getUsername());
            assertEquals(token, result.getJwt());
        }*/
    }