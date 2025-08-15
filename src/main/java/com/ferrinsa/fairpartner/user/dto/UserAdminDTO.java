package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UserAdminDTO(
        @NotBlank Long id,
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank LocalDate registrationDate
) {

    public static UserAdminDTO of(User user) {
        return new UserAdminDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRegistrationDate());
    }


}
