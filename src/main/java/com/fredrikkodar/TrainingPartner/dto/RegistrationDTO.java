package com.fredrikkodar.TrainingPartner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class RegistrationDTO {

    private String username;
    private String password;

    public RegistrationDTO() {super();}

    public RegistrationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Registration info: \n" +
                "username: " + this.username + "\n" +
                "password: " + this.password + "\n";
    }

}
