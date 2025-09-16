package com.ferrinsa.fairpartner.exception.user;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserLoginFailedException extends AppException {

    public UserLoginFailedException() {
        super("BAD_CREDENTIALS", "Error al iniciar sesión");
    }
}
