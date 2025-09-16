package com.ferrinsa.fairpartner.exception.user;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserPasswordCheckException extends AppException {
    public UserPasswordCheckException(){
        super("INVALID_PASSWORD", "La contraseña introducida no es la correcta");
    }
}
