package com.ferrinsa.fairpartner.expense.dto.invitation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInvitationRequestDTO(
        @NotNull Long expenseGroupId,
        @NotBlank @Email String email
) { }
