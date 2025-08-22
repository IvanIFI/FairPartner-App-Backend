package com.ferrinsa.fairpartner.security.role.service;

import com.ferrinsa.fairpartner.exception.role.RoleNotFoundException;
import com.ferrinsa.fairpartner.security.role.model.Role;
import com.ferrinsa.fairpartner.security.role.repository.RoleRepository;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(UserRoles roleName){
        return roleRepository.findByUserRole(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }



}
