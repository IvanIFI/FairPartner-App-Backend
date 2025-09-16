package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserPasswordRequestDTO(
        @NotBlank String oldPassword,
        @NotBlank @ValidPassword String newPassword,
        @NotBlank @ValidPassword String confirmNewPassword
) {
}
