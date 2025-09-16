package com.ferrinsa.fairpartner.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserUpdateAdminRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotEmpty List<String> roles) {
}
