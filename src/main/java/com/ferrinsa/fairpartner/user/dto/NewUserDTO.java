package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NewUserDTO(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank @Size(min=8) String password
) { }
