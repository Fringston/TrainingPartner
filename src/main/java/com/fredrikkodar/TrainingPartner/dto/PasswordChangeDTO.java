package com.fredrikkodar.TrainingPartner.dto;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
    private String userName;

}