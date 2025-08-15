package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String password
) {
    public static LoginRequestDTO of (User user){
        return new LoginRequestDTO(user.getEmail(),user.getPassword());
    }

}
