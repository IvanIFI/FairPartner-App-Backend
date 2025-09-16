package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank @ValidPassword String password
) { }
