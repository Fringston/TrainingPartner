package com.fredrikkodar.TrainingPartner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class RegistrationDTO {

    private String username;
    private String password;
    private String email;

    public RegistrationDTO() {super();}

    public RegistrationDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Registration info: \n" +
                "username: " + this.username + "\n" +
                "password: " + this.password + "\n" +
                "email: " + this.email + "\n";
    }

}
