package com.fredrikkodar.TrainingPartner.service;

import com.fredrikkodar.TrainingPartner.dto.RoleDTO;
import com.fredrikkodar.TrainingPartner.dto.UserDTO;
import com.fredrikkodar.TrainingPartner.entities.Role;
import com.fredrikkodar.TrainingPartner.entities.User;
import com.fredrikkodar.TrainingPartner.exceptions.UnauthorizedException;
import com.fredrikkodar.TrainingPartner.exceptions.UserNotFoundException;
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

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);


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

        Optional<Role> optionalAdminRole = roleRepository.findByAuthority("ADMIN");
        Role adminAuthority;
        if (optionalAdminRole.isPresent()) {
            // If the admin role already exists in the database, use it
            adminAuthority = optionalAdminRole.get();
        } else {
            // If the admin role does not exist in the database, create and save it
            adminAuthority = new Role("ADMIN");
            roleRepository.save(adminAuthority);
        }

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

}
