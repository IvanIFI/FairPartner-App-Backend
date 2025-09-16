package com.ferrinsa.fairpartner.exception.role;

import com.ferrinsa.fairpartner.exception.AppException;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;

public class RoleNotFoundException extends AppException {

    public RoleNotFoundException(UserRoles role) {
        super("ROLE_NOT_FOUND", "El rol " + role.name() + " no existe en la base de datos");
    }

}

