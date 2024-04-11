package com.fredrikkodar.TrainingPartner.dto;

import com.fredrikkodar.TrainingPartner.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long userId;
    private String username;
    private Set<RoleDTO> roles;


}
