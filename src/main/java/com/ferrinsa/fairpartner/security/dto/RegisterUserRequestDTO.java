package com.ferrinsa.fairpartner.security.dto;

import com.ferrinsa.fairpartner.security.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequestDTO(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank @ValidPassword String password
) { }
