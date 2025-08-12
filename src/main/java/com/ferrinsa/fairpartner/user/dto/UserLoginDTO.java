package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank @Email String email,
        @NotBlank String password
) {
    public static UserLoginDTO of (User user){
        return new UserLoginDTO(user.getEmail(),user.getPassword());
    }

}
