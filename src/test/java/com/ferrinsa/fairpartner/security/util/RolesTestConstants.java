package com.ferrinsa.fairpartner.security.util;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;

import java.util.List;
import java.util.Set;

public class RolesTestConstants {

    public static final RoleEntity ROLE_USER = new RoleEntity(UserRoles.USER);
    public static final Set<RoleEntity> DEFAULT_ROLES_SET = Set.of(ROLE_USER);
    public static final List<String> DEFAULT_ROLE_LIST = List.of(UserRoles.USER.name());
    public static final List<String> ADMIN_ROLE_LIST = List.of(UserRoles.ADMIN.name());
}
