package com.ferrinsa.fairpartner.exception.user;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserEmailAlreadyExistsException extends AppException {

    public UserEmailAlreadyExistsException() {
        super("EMAIL_ALREADY_EXISTS", "Ya existe un usuario con este correo electrónico");
    }
}
