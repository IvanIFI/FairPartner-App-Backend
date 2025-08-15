package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.user.model.User;

public record UserLoginResponseDTO(
        Long id,
        String name,
        String email,
        String token
) {
    public static UserLoginResponseDTO of(User user, String token) {
        return new UserLoginResponseDTO(user.getId(), user.getName(), user.getEmail(), token);
    }
}
