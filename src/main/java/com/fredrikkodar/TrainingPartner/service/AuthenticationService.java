package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.LoginResponseDTO;
import com.fredrikkodar.TrainingPartner.entities.Role;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.repository.RoleRepository;
import com.fredrikkodar.TrainingPartner.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;


   public User registerUser(String username, String password) {
       String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
       Pattern pattern = Pattern.compile(passwordPattern);
       Matcher matcher = pattern.matcher(password);

       if (!matcher.matches()) {
              throw new IllegalArgumentException("Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and be at least 8 characters long");
       }

       String encodedPassword = passwordEncoder.encode(password);
       Role userRole = roleRepository.findByAuthority("USER").get();

       Set<Role> authorities = new HashSet<>();
       authorities.add(userRole);

       return userRepository.save(new User(0L, username, encodedPassword, authorities));
       }

    public LoginResponseDTO loginUser(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }
}
