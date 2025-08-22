package com.ferrinsa.fairpartner.exception.user;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserNotFoundException extends AppException {

    public UserNotFoundException(String value) {
        super("USER_NOT_FOUND", "No se ha encontrado el usuario: " + value);
    }

}