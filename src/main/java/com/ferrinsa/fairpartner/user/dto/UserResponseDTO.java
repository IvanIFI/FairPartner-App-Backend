package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        List<String> roles
) {
    public static UserResponseDTO of(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(RoleEntity::getRoleName).toList());
    }
}
