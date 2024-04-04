package com.fredrikkodar.TrainingPartner.dto;

import com.fredrikkodar.TrainingPartner.models.User;
import lombok.Data;

@Data
public class LoginResponseDTO {

    private User user;
    private String jwt;

    public LoginResponseDTO(User user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }
}
