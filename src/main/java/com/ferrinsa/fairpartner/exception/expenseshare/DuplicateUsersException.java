package com.ferrinsa.fairpartner.exception.expenseshare;

import com.ferrinsa.fairpartner.exception.AppException;

public class DuplicateUsersException extends AppException {

    public DuplicateUsersException() {
        super("DUPLICATE_USERS", "No se puede repartir el gasto a un mismo usuario duplicado");
    }

}
