package com.ferrinsa.fairpartner.exception.user;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserFailedUpdateProfileException extends AppException {

    public UserFailedUpdateProfileException(String message) {
        super("NEW_DATA_INVALID", message);
    }
}
