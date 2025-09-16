package com.ferrinsa.fairpartner.security.dto;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.util.List;


public record LoginResponseDTO(
        Long id,
        String name,
        String email,
        List<String> roles,
        String token
) {
    public static LoginResponseDTO of(UserEntity user, String token) {
        return new LoginResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(RoleEntity::getRoleName).toList(),
                token);
    }
}
