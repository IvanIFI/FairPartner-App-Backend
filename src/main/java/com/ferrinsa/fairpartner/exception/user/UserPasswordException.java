package com.ferrinsa.fairpartner.exception.user;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserPasswordException extends AppException {
    public UserPasswordException() {
        super("PASSWORD_NOT_MATCHES", "Contraseña introducida no coincide");
    }
}
