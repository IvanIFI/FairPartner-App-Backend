package com.ferrinsa.fairpartner.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull LocalDate registrationDate
) { }
