package com.ferrinsa.fairpartner.security.role.repository;

import com.ferrinsa.fairpartner.security.role.model.Role;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByUserRole(UserRoles roleName);

}
