package com.ferrinsa.fairpartner.security.role.repository;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

    Optional<RoleEntity> findByUserRole(UserRoles roleName);

}
