package com.ferrinsa.fairpartner.user.dto;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.time.LocalDate;
import java.util.List;

public record UserAdminResponseDTO(
        Long id,
        String name,
        String email,
        String password,
        LocalDate registrationDate,
        List<String>roles
) {

    public static UserAdminResponseDTO of(UserEntity user) {
        return new UserAdminResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRegistrationDate(),
                user.getRoles().stream().map(RoleEntity::getRoleName).toList());
    }

}
