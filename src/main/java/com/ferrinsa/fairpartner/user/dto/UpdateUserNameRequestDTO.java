package com.ferrinsa.fairpartner.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserNameRequestDTO(
        @NotBlank String name
) {
}
